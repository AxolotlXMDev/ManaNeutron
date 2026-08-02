package org.a8043.gui.views;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import github.axolotl.ai.content.AssistantContent;
import github.axolotl.ai.content.SystemContent;
import github.axolotl.ai.content.ToolContent;
import github.axolotl.ai.content.UserContent;
import github.axolotl.ai.session.TaskStatus;
import github.axolotl.ai.sse.ToolCallResultDO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.a8043.gui.Main;
import org.a8043.gui.ServerDataGetter;
import org.a8043.gui.session.ContentRenderer;
import org.a8043.gui.session.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SessionTab {
        private static final List<ContentRenderer<?>> contentRenderers = new ArrayList<>();
        private static final List<Status> allStatusList = new ArrayList<>();
        private static final List<Status> statusList = new ArrayList<>();

        public static <T> void registerContentRenderer(ContentRenderer<T> renderer) {
                contentRenderers.add(renderer);
        }

        public static void registerStatus(Status status) {
                allStatusList.add(status);
        }

        public static void registerVisibleStatus(String name) {
                statusList.add(allStatusList.stream().filter(s -> s.getName().equals(name))
                        .findFirst().orElse(null));
        }

        public static final Map<AssistantContent, StringProperty> ASSISTANT_CONTENTS = new HashMap<>();
        private final String sessionId;
        private final Map<Status, StringProperty> statusContents = new HashMap<>();
        @FXML
        private ListView<Object> contentList;
        @FXML
        private VBox workStatusBox;
        @FXML
        private StackPane inputPane;
        @FXML
        private TextArea textArea;
        @FXML
        private HBox statusLine;
        private AssistantMessage currentAssistantMessage;

        @FXML
        private void initialize() {
                contentList.setCellFactory(param -> new ListCell<>() {
                        @Override
                        protected void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                        ContentRenderer<?> renderer = contentRenderers.stream()
                                                .filter(r -> r.getType().equals(item.getClass()))
                                                .findFirst().orElse(null);
                                        if (renderer != null) {
                                                setGraphic(((ContentRenderer<Object>) renderer).render(
                                                        ServerDataGetter.getSessionById(sessionId), item));
                                        } else {
                                                log.warn("Missing session content renderer: {}", item.getClass());
                                        }
                                } else {
                                        setGraphic(null);
                                }
                        }
                });
                contentList.getItems().addAll(ServerDataGetter.getSessionById(sessionId)
                        .getContents().stream().filter(c -> !(c instanceof SystemContent)).toList());

                statusList.forEach(status -> {
                        StringProperty content = new SimpleStringProperty();
                        statusContents.put(status, content);
                        statusLine.getChildren().add(new Button() {{
                                textProperty().bind(content);
                                getStyleClass().addAll("session-status", status.getName());
                                setOnAction(e -> status.onClick(ServerDataGetter.getSessionById(sessionId), e, content));
                        }});
                });
                updateStatus();

                Runnable newAssistantMessage = () -> {
                        if (currentAssistantMessage != null) {
                                return;
                        }

                        AssistantContent content = new AssistantContent();
                        SimpleStringProperty contentText = new SimpleStringProperty("");
                        ASSISTANT_CONTENTS.put(content, contentText);
                        contentList.getItems().add(content);
                        currentAssistantMessage = new AssistantMessage(content, contentText);
                };
                Main.instance.getCurrentClient().regMessageEmitter(sessionId).setOnMessage(event -> Platform.runLater(() -> {
                        JSONObject json = (JSONObject) JSON.parse(event.data());
                        switch (json.getString("@type")) {
                                case "github.axolotl.ai.sse.CompleteResponseDO" -> currentAssistantMessage = null;
                                case "github.axolotl.ai.sse.PartialResponseDO" -> {
                                        newAssistantMessage.run();
                                        currentAssistantMessage.content.setValue(
                                                currentAssistantMessage.content.getValue() +
                                                json.getString("content")
                                        );
                                }
                                case "github.axolotl.ai.sse.ToolCallResultDO" -> {
                                        currentAssistantMessage = null;
                                        ToolCallResultDO result = json.to(ToolCallResultDO.class);
                                        contentList.getItems().add(new ToolContent(
                                                (String) result.getResult(),
                                                result.getRequestId(),
                                                Main.instance.getCurrentClient()
                                                        .getRequestById(result.getRequestId()).getName(),
                                                !result.isError()
                                        ));
                                }
                                default -> log.warn("Unknown SSE event: {}", event.event());
                        }
                })).asyncListen();
        }

        public void updateStatus() {
                statusContents.forEach((s, c) -> c.setValue(s.update(ServerDataGetter.getSessionById(sessionId))));
        }

        @FXML
        private void send() {
                String text = textArea.getText();
                if (text.isBlank()) {
                        return;
                }

                if (!Main.instance.getCurrentClient().insertUserMessage(sessionId, text)) {
                        log.warn("Failed to send user message: {}", text);
                        return;
                }
                List<TaskStatus> taskStatuses = ServerDataGetter.getSessionById(sessionId).getTaskStatuses();
                if (taskStatuses == null || taskStatuses.stream().allMatch(TaskStatus::isFinished)) {
                        Main.instance.getCurrentClient().startAgentLoop(sessionId, true);
                }

                textArea.clear();
                contentList.getItems().add(new UserContent(text));
        }

        @Value
        private static class AssistantMessage {
                AssistantContent message;
                StringProperty content;
        }
}

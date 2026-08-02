package org.a8043.gui.views;

import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import org.a8043.gui.I18n;
import org.a8043.gui.Main;
import org.a8043.gui.ServerDataGetter;
import org.a8043.gui.util.SearchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MainView {
        @FXML
        private TabPane pane;
        @FXML
        private TreeView<Object> sessionsTree;
        @FXML
        private TextField searchField;
        private List<SessionInfo> sessionList;
        private Map<String, List<SessionInfo>> sessions;

        @FXML
        private void initialize() {
                sessionsTree.setCellFactory(param -> new TreeCell<>() {
                        @Override
                        protected void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                        if (item instanceof SessionInfo session) {
                                                setGraphic(new Label(session.getName()));
                                        } else if (item instanceof String str) {
                                                setGraphic(new Label(str));
                                        }
                                } else {
                                        setGraphic(null);
                                }
                        }
                });

                refresh();
                show("");
        }

        public void refresh() {
                ServerDataGetter.cleanCache();

                sessions = new HashMap<>();
                sessionList = ServerDataGetter.getSessionInfos().getSessionInfos();
                sessionList.forEach(s -> sessions.computeIfAbsent(s.getWorkDir(), k -> new ArrayList<>()).add(s));
        }

        public void show(String keyword) {
                List<SessionInfo> list;
                if (keyword.isEmpty()) {
                        list = sessionList;
                } else {
                        list = SearchUtil.search(sessionList, SessionInfo::getName, keyword);
                }
                sessionsTree.setRoot(new TreeItem<>(I18n.get("main.sessions")) {{
                        sessions.forEach((k, v) -> {
                                TreeItem<Object> sessionsItem = new TreeItem<>(k);
                                v.forEach(s -> {
                                        if (list.contains(s)) {
                                                sessionsItem.getChildren().add(new TreeItem<>(s));
                                        }
                                });
                                getChildren().add(sessionsItem);
                        });
                }});
        }

        public void search(InputMethodEvent event) {
                show(((TextField) event.getSource()).getText());
        }

        @FXML
        private void newSession() {
                AtomicReference<Main.ModalController<?>> modal = new AtomicReference<>();
                modal.set(Main.instance.showModal(I18n.get("main.newSession"), NewSessionModal.class,
                        (Consumer<Session>) session -> {
                                refresh();
                                show(searchField.getText());
                                openSession(session);
                                modal.get().close();
                        }));
        }

        @FXML
        private void onSessionsClick(MouseEvent event) {
                if (event.getClickCount() != 2) {
                        return;
                }

                TreeItem<Object> selected = sessionsTree.getSelectionModel().getSelectedItem();
                if (selected == null || !(selected.getValue() instanceof SessionInfo sessionInfo)) {
                        return;
                }

                openSession(ServerDataGetter.getSessionById(sessionInfo.getId()));
        }

        public void openSession(Session session) {
                pane.getTabs().add(new Tab(session.getInfo().getName(),
                        Main.loadFxml(SessionTab.class, session.dgetId())));
        }
}

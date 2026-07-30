package org.a8043.gui.views;

import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import org.a8043.gui.I18n;
import org.a8043.gui.Main;
import org.a8043.gui.util.SearchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                        if (item instanceof Session session) {
                                                setGraphic(new Label(session.getInfo().getName()));
                                        } else if (item instanceof String str) {
                                                setGraphic(new Label(str));
                                        }
                                } else {
                                        setGraphic(null);
                                }
                        }
                });
                sessionsTree.setShowRoot(true);

                refresh();
                show("");
        }

        public void refresh() {
                sessions = new HashMap<>();
                sessionList = Main.instance.getCurrentClient().getSessions().getSessionInfos();
                sessionList.forEach(s -> sessions.computeIfAbsent(s.getWorkDir(), k -> new ArrayList<>()).add(s));
        }

        public void show(String keyword) {
                List<SessionInfo> list;
                if (keyword.isEmpty()) {
                        list = sessionList;
                } else {
                        list = SearchUtil.search(sessionList, SessionInfo::getName, keyword);
                }
                sessionsTree.setRoot(new TreeItem<>() {{
                        sessions.forEach((k, v) -> {
                                TreeItem<Object> sessionsItem = new TreeItem<>(k);
                                v.forEach(s -> {
                                        if (list.contains(s)) {
                                                sessionsItem.getChildren().add(new TreeItem<>(s));
                                        }
                                });
                        });
                }});
        }

        public void search(InputMethodEvent event) {
                show(((TextField) event.getSource()).getText());
        }

        @FXML
        private void newSession() {
                Main.instance.showModal(I18n.get("main.newSession"), NewSessionModal.class,
                        (Runnable) () -> {
                                refresh();
                                show(searchField.getText());
                        });
        }

        @FXML
        private void openSession() {
        }
}

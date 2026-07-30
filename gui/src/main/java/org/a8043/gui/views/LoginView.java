package org.a8043.gui.views;

import com.alibaba.fastjson2.JSONArray;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.a8043.gui.Client;
import org.a8043.gui.I18n;
import org.a8043.gui.Main;
import org.a8043.gui.Server;

import java.util.Objects;

public class LoginView {
        @FXML
        private TextField ipField;
        @FXML
        private PasswordField tokenField;
        @FXML
        private Button loginButton;
        @FXML
        private ListView<Server> recentlyLoginList;

        @FXML
        private void initialize() {
                recentlyLoginList.setCellFactory(param -> new ListCell<>() {
                        @Override
                        protected void updateItem(Server item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                        setGraphic(new BorderPane() {{
                                                setLeft(new Label(item.getIp()));
                                                setRight(new Button(I18n.get("login.login")) {{
                                                        setOnAction(e -> {
                                                                ipField.setText(item.getIp());
                                                                tokenField.setText(item.getToken());
                                                                login();
                                                        });
                                                }});
                                        }});
                                } else {
                                        setGraphic(null);
                                }
                        }
                });
                recentlyLoginList.setItems(FXCollections.observableList(
                        Main.instance.getProperties().getList("recentlyLogin", Server.class)));
        }

        @FXML
        private void login() {
                String ip = ipField.getText();
                String token = tokenField.getText();
                if (ip.isBlank() || token.isBlank()) {
                        Main.instance.showTipModal(I18n.get("login.empty"));
                        return;
                }

                loginButton.setDisable(true);

                new Thread(() -> {
                        Server server = new Server(ip, token);
                        Client client = server.createClient();
                        try {
                                client.hello();
                        } catch (Exception e) {
                                Platform.runLater(() -> {
                                        loginButton.setDisable(false);
                                        Main.instance.showTipModal(I18n.get("login.failure", e.getMessage()));
                                });
                                return;
                        }

                        JSONArray servers = (JSONArray) Main.instance.getProperties().computeIfAbsent(
                                "recentlyLogin", k -> new JSONArray());
                        if (!Objects.requireNonNull(servers).contains(server)) {
                                servers.add(server);
                        }

                        Platform.runLater(() -> {
                                Main.instance.setCurrentClient(client);
                                Main.instance.display(MainView.class);
                        });
                }).start();
        }
}

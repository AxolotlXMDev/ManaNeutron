package org.a8043.gui;

import animatefx.animation.FadeIn;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson2.JSONObject;
import com.dtflys.forest.Forest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.a8043.gui.views.LoginView;

import java.io.File;

@Getter
public class Main extends Application {
        public static Main instance;
        private JSONObject properties;
        private JSONObject settings;
        @Setter
        private Client currentClient;
        private final StackPane pane = new StackPane();

        public Main() {
                instance = this;
        }

        @Override
        public void start(Stage stage) throws Exception {
                Forest.config().setLogResponseContent(true);
                File propertiesFile = new File("./config/properties.json");
                properties = propertiesFile.exists() ?
                        JSONObject.parse(FileUtil.readUtf8String(propertiesFile)) : new JSONObject();
                File settingsFile = new File("./config/settings.json");
                settings = settingsFile.exists() ?
                        JSONObject.parse(FileUtil.readUtf8String(settingsFile)) : new JSONObject();

                setUserAgentStylesheet(ResourceUtil.getResource("styles/" +
                                                                settings.computeIfAbsent("style", k -> "light") +
                                                                ".css").toString());

                stage.setTitle("ManaNeutron");
                JSONObject window = (JSONObject) properties.computeIfAbsent("window", k -> new JSONObject());
                stage.setWidth((Integer) window.computeIfAbsent("width", k -> 1000));
                stage.setHeight((Integer) window.computeIfAbsent("height", k -> 600));
                stage.setScene(new Scene(pane));
                display(LoginView.class);
                stage.show();
        }

        @Override
        public void stop() throws Exception {
                FileUtil.writeUtf8String(properties.toString(), new File("./config/properties.json"));
                FileUtil.writeUtf8String(settings.toString(), new File("./config/settings.json"));
        }

        public void display(Class<?> clazz) {
                display(loadFxml(clazz));
        }

        @SneakyThrows
        private static Node loadFxml(Class<?> clazz, Object... args) {
                FXMLLoader loader = new FXMLLoader(clazz.getResource(clazz.getSimpleName() + ".fxml"));
                loader.setResources(I18n.getLangBundle());
                loader.setControllerFactory(c -> {
                        try {
                                return c.getConstructors()[0].newInstance(args);
                        } catch (Exception e) {
                                throw new RuntimeException(e);
                        }
                });
                return loader.load();
        }

        public void display(Node node) {
                if (pane.getChildren().isEmpty()) {
                        pane.getChildren().add(node);
                } else {
                        pane.getChildren().set(0, node);
                }
        }

        public <N extends Node> ModalController<N> showModal(String name, N node) {
                VBox modal = new VBox();
                modal.getStyleClass().add("modal");

                Button closeButton = new Button("x");
                closeButton.getStyleClass().add("modal-close-button");

                BorderPane titleBar = new BorderPane(new Label(name),
                        null, closeButton, null, null);
                titleBar.setMaxHeight(5);
                modal.getChildren().addAll(titleBar, new Separator());

                modal.getChildren().add(node);

                AnchorPane modalPane = new AnchorPane(modal);
                modalPane.getStyleClass().add("modal-bg");
                ModalController<N> controller = new ModalController<>(node, modalPane);

                node.layoutBoundsProperty().addListener((obd, oldValue, newValue) -> {
                        double x = (pane.getWidth() - newValue.getWidth()) / 2;
                        AnchorPane.setRightAnchor(modal, pane.getWidth() - (x + newValue.getWidth()));
                        AnchorPane.setLeftAnchor(modal, x);

                        double y = (pane.getHeight() - newValue.getHeight()) / 2;
                        AnchorPane.setTopAnchor(modal, y);
                        AnchorPane.setBottomAnchor(modal, pane.getHeight() - (y + newValue.getHeight()));
                });

                closeButton.setOnAction(e -> controller.close());

                pane.getChildren().add(modalPane);
                new FadeIn(modalPane).play();
                return controller;
        }

        public void showTipModal(String text) {
                Label label = new Label(text);
                label.setWrapText(true);
                label.setFont(new Font(12));
                Button button = new Button("确定");
                VBox box = new VBox(label, button);
                box.setAlignment(Pos.CENTER);
                ModalController<VBox> modalController = showModal(I18n.get("info"), box);
                button.setOnAction(e -> modalController.close());
        }

        public ModalController<?> showModal(String name, Class<?> clazz, Object... args) {
                return showModal(name, loadFxml(clazz, args));
        }

        @Getter
        public class ModalController<N extends Node> {
                private final N node;
                private final AnchorPane modalPane;
                @Setter
                private Runnable onClose;

                public ModalController(N node, AnchorPane modalPane) {
                        this.node = node;
                        this.modalPane = modalPane;
                }

                public void close() {
                        pane.getChildren().remove(modalPane);
                        if (onClose != null) {
                                onClose.run();
                        }
                }
        }
}

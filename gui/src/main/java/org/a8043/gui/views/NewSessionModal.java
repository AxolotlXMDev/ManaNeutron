package org.a8043.gui.views;

import github.axolotl.ai.session.SessionInfo;
import github.axolotl.setting.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.a8043.gui.I18n;
import org.a8043.gui.Main;

@RequiredArgsConstructor
public class NewSessionModal {
        private final Runnable onCreate;
        @FXML
        private ComboBox<Model> modelBox;
        @FXML
        private TextField workDirField;
        @FXML
        private TextField nameField;
        @FXML
        private ComboBox<ReasoningEffort> effortBox;

        @FXML
        private void initialize() {
                Settings settings = Main.instance.getCurrentClient().getSettings();
                SessionInfo last = Main.instance.getCurrentClient().getLastSessionInfo();
                settings.getProviders().forEach(p -> p.getModelIds().forEach(m ->
                        modelBox.getItems().add(new Model(p, m))));
                effortBox.getItems().addAll(ReasoningEffort.values());
                modelBox.getSelectionModel().select(modelBox.getItems().stream()
                        .filter(m -> m.getModelId().equals(settings.getModelId()))
                        .findFirst().orElse(null));
                effortBox.getSelectionModel().select(settings.getEffort());
                nameField.setText("New Session");
                workDirField.setText(last != null ? last.getWorkDir() : "");

                Callback<ListView<Model>, ListCell<Model>> call = param -> new ListCell<>() {
                        @Override
                        protected void updateItem(Model item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                        setGraphic(new HBox(new Label(item.getProvider().getName()) {{
                                                setStyle("-fx-text-fill: rgb(119 119 119)");
                                        }}, new Label(item.getModelId().getName())) {{
                                                setPadding(new Insets(2));
                                        }});
                                } else {
                                        setGraphic(null);
                                }
                        }
                };
                modelBox.setCellFactory(param -> new ListCell<>() {
                        @Override
                        protected void updateItem(Model item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                        setGraphic(new HBox(new Label(item.getProvider().getName()) {{
                                                setStyle("-fx-text-fill: rgb(119 119 119)");
                                        }}, new Label(item.getModelId().getName())) {{
                                                setPadding(new Insets(2));
                                        }});
                                } else {
                                        setGraphic(null);
                                }
                        }
                });
                modelBox.setButtonCell(call.call(null));
        }

        @FXML
        private void create() {
                Model model = modelBox.getSelectionModel().getSelectedItem();
                String workDir = workDirField.getText();
                String name = nameField.getText();
                ReasoningEffort effort = effortBox.getSelectionModel().getSelectedItem();
                if (model == null || workDir.isBlank() || name.isBlank() || effort == null) {
                        Main.instance.showTipModal(I18n.get("main.newSession.empty"));
                        return;
                }

                Main.instance.getCurrentClient().newSession(name, workDir,
                        new ModelChoice(model.getProvider().getName(), model.getModelId(), effort));
                onCreate.run();
        }

        @Value
        private static class Model {
                Provider provider;
                ModelId modelId;
        }
}

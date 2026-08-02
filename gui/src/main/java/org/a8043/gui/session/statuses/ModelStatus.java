package org.a8043.gui.session.statuses;

import com.google.auto.service.AutoService;
import github.axolotl.ai.session.Session;
import github.axolotl.setting.ModelChoice;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Popup;
import org.a8043.gui.Model;
import org.a8043.gui.ServerDataGetter;
import org.a8043.gui.session.Status;

@AutoService(Status.class)
public class ModelStatus implements Status {
        @Override
        public String getName() {
                return "model";
        }

        @Override
        public String update(Session session) {
                ModelChoice modelChoice = session.getInfo().getModelChoice();
                return "[" + modelChoice.getProviderName() + "] " +
                       modelChoice.getModelId().getName();
        }

        @Override
        public void onClick(Session session, ActionEvent event, StringProperty content) {
                ListView<Model> listView = new ListView<>();
                Popup popup = new Popup();
                popup.getContent().add(listView);

                listView.setOnMouseClicked(e -> {
                        Model selected = listView.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                                session.getInfo().getModelChoice().setProviderName(selected.getProvider().getName());
                                session.getInfo().getModelChoice().setModelId(selected.getModelId());
                                content.setValue(update(session));
                                popup.hide();
                        }
                });
                listView.setCellFactory(param -> new ListCell<>() {
                        @Override
                        protected void updateItem(Model item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                        setGraphic(new Label("[" + item.getProvider().getName() + "] " +
                                                             item.getModelId().getName()));
                                } else {
                                        setGraphic(null);
                                }
                        }
                });
                listView.setItems(FXCollections.observableArrayList(ServerDataGetter.getModels()));

                Button button = (Button) event.getSource();
                double x = button.localToScreen(0, 0).getX();
                double y = button.localToScreen(0, 0).getY() + button.getHeight();
                popup.show(button.getScene().getWindow(), x, y);
        }
}

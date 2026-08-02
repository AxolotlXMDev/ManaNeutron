package org.a8043.gui.session.statuses;

import com.google.auto.service.AutoService;
import github.axolotl.ai.session.Session;
import github.axolotl.setting.ReasoningEffort;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Popup;
import org.a8043.gui.session.Status;
import org.a8043.gui.util.LightScratch;

@AutoService(Status.class)
public class EffortStatus implements Status {
        @Override
        public String getName() {
                return "effort";
        }

        @Override
        public String update(Session session) {
                return session.getInfo().getModelChoice().getEffort().name();
        }

        @Override
        public void onClick(Session session, ActionEvent event, StringProperty content) {
                ListView<ReasoningEffort> listView = new ListView<>();
                Popup popup = new Popup();
                popup.getContent().add(listView);

                listView.setOnMouseClicked(e -> {
                        ReasoningEffort selected = listView.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                                session.getInfo().getModelChoice().setEffort(selected);
                                content.setValue(update(session));
                                popup.hide();
                        }
                });
                listView.setCellFactory(param -> new ListCell<>() {
                        @Override
                        protected void updateItem(ReasoningEffort item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                        Label label = new Label(item.name());
                                        if (item == ReasoningEffort.ULTRA) {
                                                new LightScratch(label, LightScratch.RAINBOW_COLORS, 2).start();
                                        }
                                        setGraphic(label);
                                } else {
                                        setGraphic(null);
                                }
                        }
                });
                listView.setItems(FXCollections.observableArrayList(ReasoningEffort.values()));

                Button button = (Button) event.getSource();
                double x = button.localToScreen(0, 0).getX();
                double y = button.localToScreen(0, 0).getY() + button.getHeight();
                popup.show(button.getScene().getWindow(), x, y);
        }
}

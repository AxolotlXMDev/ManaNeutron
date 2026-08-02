package org.a8043.gui.session.contentRenderers;

import com.google.auto.service.AutoService;
import github.axolotl.ai.content.UserContent;
import github.axolotl.ai.session.Session;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.a8043.gui.I18n;
import org.a8043.gui.Main;
import org.a8043.gui.session.ContentRenderer;

@AutoService(ContentRenderer.class)
public class UserContentRenderer implements ContentRenderer<UserContent> {
        @Override
        public Node render(Session session, UserContent content) {
                VBox box = new VBox();
                box.getStyleClass().add("user-content");
                box.setMaxWidth(300);
                box.setMinWidth(300);
                box.setPrefWidth(300);
                Label label = new Label(content.getContent()) {{
                        setWrapText(true);
                        setMaxWidth(290);
                        setMinWidth(290);
                        setPrefWidth(290);
                }};
                box.getChildren().add(label);

                box.setOnMouseClicked(e -> {
                        if (e.getButton() != MouseButton.SECONDARY) {
                                return;
                        }

                        ContextMenu menu = new ContextMenu();
                        menu.getItems().add(new MenuItem(I18n.get("session.content.edit")) {{
                                setOnAction(e1 -> {
                                        TextArea textArea = new TextArea(content.getContent());
                                        box.getChildren().setAll(textArea,
                                                new Button(I18n.get("session.content.edit")) {{
                                                        setOnAction(e2 -> {
                                                                content.setContent(textArea.getText());
                                                                Main.instance.getCurrentClient().editContent(
                                                                        session.dgetId(),
                                                                        session.getContents().indexOf(content),
                                                                        textArea.getText()
                                                                );
                                                                box.getChildren().setAll(label);
                                                        });
                                                }});
                                });
                        }});
                });

                return new HBox(box) {{
                        setAlignment(Pos.CENTER_RIGHT);
                }};
        }

        @Override
        public Class<UserContent> getType() {
                return UserContent.class;
        }
}

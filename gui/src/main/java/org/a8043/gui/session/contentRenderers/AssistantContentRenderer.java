package org.a8043.gui.session.contentRenderers;

import com.google.auto.service.AutoService;
import github.axolotl.ai.content.AssistantContent;
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
import org.a8043.gui.views.SessionTab;

@AutoService(ContentRenderer.class)
public class AssistantContentRenderer implements ContentRenderer<AssistantContent> {
        @Override
        public Node render(Session session, AssistantContent content) {
                VBox box = new VBox();
                box.setMaxWidth(700);
                box.setPrefWidth(700);
                box.setMinWidth(700);

                Label label = new Label(content.getContent()) {{
                        if (SessionTab.ASSISTANT_CONTENTS.containsKey(content)) {
                                textProperty().bind(SessionTab.ASSISTANT_CONTENTS.get(content));
                        } else {
                                setText(content.getContent());
                        }

                        setWrapText(true);
                        setMaxWidth(690);
                        setMinWidth(690);
                        setPrefWidth(690);
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
                        setAlignment(Pos.CENTER);
                }};
        }

        @Override
        public Class<AssistantContent> getType() {
                return AssistantContent.class;
        }
}

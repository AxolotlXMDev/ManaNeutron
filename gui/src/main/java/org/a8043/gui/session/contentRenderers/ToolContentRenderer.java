package org.a8043.gui.session.contentRenderers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.auto.service.AutoService;
import github.axolotl.ai.content.ToolContent;
import github.axolotl.ai.session.Session;
import github.axolotl.ai.sse.ToolExecutionRequestDO;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.a8043.gui.I18n;
import org.a8043.gui.Main;
import org.a8043.gui.session.ContentRenderer;
import org.a8043.gui.session.ToolCallRenderer;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AutoService(ContentRenderer.class)
public class ToolContentRenderer implements ContentRenderer<ToolContent> {
        private static final List<ToolCallRenderer> RENDERERS = new ArrayList<>();

        public static void registerRenderer(ToolCallRenderer renderer) {
                RENDERERS.add(renderer);
        }

        @Override
        public Node render(Session session, ToolContent content) {
                ToolExecutionRequestDO request = Main.instance.getCurrentClient().getRequestById(content.getRequestId());
                JSONObject args = (JSONObject) JSON.parse(request.getArguments());
                VBox box = new VBox();
                box.getStyleClass().add("tool-content");
                box.setMaxWidth(700);
                box.setPrefWidth(700);
                box.setMinWidth(700);

                box.getChildren().add(RENDERERS.stream()
                        .filter(r -> r.getToolName().equals(content.getName()))
                        .findFirst().orElseGet(() -> {
                                log.warn("No renderer found for tool: {}", content.getName());
                                return new ToolCallRenderer() {
                                        @Override
                                        public Node render(JSONObject args) {
                                                return new Label(I18n.get("session.tools.unknown_tool", content.getName()));
                                        }

                                        @Override
                                        public String getToolName() {
                                                return "";
                                        }
                                };
                        }).render(args));

                box.getChildren().add(new TitledPane(I18n.get("session.content.details"), new VBox(
                        new Label(I18n.get("session.content.tool.args")),
                        new SwingNode() {{
                                setContent(new RSyntaxTextArea(args.toString(JSONWriter.Feature.PrettyFormat)) {{
                                        setEditable(false);
                                        setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_JSON);
                                }});
                        }},
                        new Label(I18n.get("session.content.tool.result")),
                        new SwingNode() {{
                                setContent(new RSyntaxTextArea(content.getContent()) {{
                                        setEditable(false);
                                }});
                        }}
                )) {{
                        setExpanded(false);
                }});

                return new HBox(box) {{
                        setAlignment(Pos.CENTER);
                }};
        }

        @Override
        public Class<ToolContent> getType() {
                return ToolContent.class;
        }
}

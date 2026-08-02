package org.a8043.gui.session.toolRenderers;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.a8043.gui.I18n;
import org.a8043.gui.session.ToolCallRenderer;

@AutoService(ToolCallRenderer.class)
public class RunCommandRenderer implements ToolCallRenderer {
        @Override
        public Node render(JSONObject args) {
                return new Label(I18n.get("session.tools.run_command", args.getString("command"))) {{
                        getStyleClass().add("run-command-tool");
                }};
        }

        @Override
        public String getToolName() {
                return "run_command";
        }
}

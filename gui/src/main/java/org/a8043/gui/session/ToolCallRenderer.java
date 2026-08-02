package org.a8043.gui.session;

import com.alibaba.fastjson2.JSONObject;
import javafx.scene.Node;

public interface ToolCallRenderer {
        Node render(JSONObject args);

        String getToolName();
}

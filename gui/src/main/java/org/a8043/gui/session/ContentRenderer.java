package org.a8043.gui.session;

import github.axolotl.ai.session.Session;
import javafx.scene.Node;

public interface ContentRenderer<T> {
        Node render(Session session, T content);

        Class<T> getType();
}

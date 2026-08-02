package org.a8043.gui.session;

import github.axolotl.ai.session.Session;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;

public interface Status {
        String getName();

        String update(Session session);

        void onClick(Session session, ActionEvent event, StringProperty content);
}

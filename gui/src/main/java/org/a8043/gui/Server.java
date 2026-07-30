package org.a8043.gui;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import lombok.Value;

@Value
public class Server {
        String ip;
        String token;

        public Client createClient() {
                ForestConfiguration config = Forest.config();
                config.setVariable("url", ip);
                config.setVariable("token", "Bearer " + token);
                return config.client(Client.class);
        }
}

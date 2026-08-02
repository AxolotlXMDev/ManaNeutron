package org.a8043.gui;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import github.axolotl.ai.sse.ToolExecutionRequestDO;

import java.util.List;

public class TB {
        public static void main(String[] args) {
                ForestConfiguration config = Forest.config();
                config.setLogEnabled(false);
                config.setVariable("url", "localhost:8088");
                config.setVariable("token", "Bearer Gt8jNm9Kc22$vrsw3^v325R7%#Pv55C@xLx34&zR7%R7%32v");
                Client client = config.client(Client.class);
                List<ToolExecutionRequestDO> allRequests = client.getAllRequests();

                System.out.println("===All===");
                for (ToolExecutionRequestDO request : allRequests) {
                        System.out.println("Request: " + request);
                }
                System.out.println("=========");

                for (ToolExecutionRequestDO request : allRequests) {
                        System.out.println("ID: " + request.getId());
                        System.out.println("Request: " + client.getRequestById(request.getId()));
                        System.out.println();
                }
        }
}

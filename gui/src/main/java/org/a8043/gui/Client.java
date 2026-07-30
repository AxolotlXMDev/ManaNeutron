package org.a8043.gui;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Post;
import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.session.SessionInfos;
import github.axolotl.setting.ModelChoice;
import github.axolotl.setting.Settings;

@BaseRequest(
        baseURL = "${url}",
        headers = {
                "Authorization: ${token}",
                "Content-Type: application/json"
        }
)
public interface Client {
        @Get("/hello")
        String hello();

        @Get("/settings/getSetting")
        Settings getSettings();

        @Get("/sessions/getSessionInfos")
        SessionInfos getSessions();

        @Get("/sessions/getLastSessionInfo")
        SessionInfo getLastSessionInfo();

        @Post("/sessions/newSession")
        Session newSession(@Body("name") String name,
                           @Body("workDir") String workDir,
                           @Body("modelChoice") ModelChoice modelChoice);
}

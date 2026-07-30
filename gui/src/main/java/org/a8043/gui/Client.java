package org.a8043.gui;

import com.dtflys.forest.annotation.*;
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

        @Post("/sessions/editContent")
        Session editContent(@Body("`sessionId") String sessionId,
                            @Body("contentIndex") int contentIndex,
                            @Body("content") String content);

        @Post("/sessions/sendUserMessage")
        Session sendUserMessage(@Query("sessionId") String sessionId,
                                @Query("content") String content);
}

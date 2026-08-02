package org.a8043.gui;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestSSE;
import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.session.SessionInfos;
import github.axolotl.ai.sse.ToolExecutionRequestDO;
import github.axolotl.setting.ModelChoice;
import github.axolotl.setting.Settings;

import java.util.List;

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
        Session editContent(@Body("sessionId") String sessionId,
                            @Body("contentIndex") int contentIndex,
                            @Body("content") String content);

        @Get("/sessions/insertUserMessage")
        boolean insertUserMessage(@Query("sessionId") String sessionId,
                                  @Query("content") String content);

        @Get("/sessions/startAgentLoop")
        void startAgentLoop(@Query("sessionId") String sessionId,
                               @Query("isStartNewTask") boolean isStartNewTask);

        @Get("/sse/regMessageEmitter")
        ForestSSE regMessageEmitter(@Query("sessionId") String sessionId);

        @Get("/sessions/getSessionById")
        Session getSessionById(@Query("sessionId") String sessionId);

        @Get("/requests/getRequestById")
        ToolExecutionRequestDO getRequestById(@Query("id") String id);

        @Get("/requests/getRequestsByIds")
        List<ToolExecutionRequestDO> getRequestsByIds(@Query("ids") List<String> ids);
}

package org.a8043.gui;

import com.dtflys.forest.annotation.*;
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
        Session editContent(@Body("`sessionId") String sessionId,
                            @Body("contentIndex") int contentIndex,
                            @Body("content") String content);

        @Post("/sessions/insertUserMessage")
        Session insertUserMessage(@Query("sessionId") String sessionId,
                                  @Query("content") String content);

        @Post("/sessions/startAgentLoop")
        Session startAgentLoop(@Query("sessionId") String sessionId,
                               @Query("isStartNewTask") boolean isStartNewTask);

        @Get("/sessions/getSessionById")
        Session getSessionById(@Query("sessionId") String sessionId);

        @Get("/requests/getRequestById")
        ToolExecutionRequestDO getRequestById(@Query("id") String id);

        @Post("/requests/getRequestsByIds")
        List<ToolExecutionRequestDO> getRequestsByIds(@Query("ids") List<String> ids);

        @Get("/requests/getAll")
        List<ToolExecutionRequestDO> getAllRequests();
}

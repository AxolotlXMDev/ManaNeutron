package github.axolotl.backend.controller;

import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.sse.ToolExecutionRequestDO;
import github.axolotl.backend.service.ToolExecutionRequestDOService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/requests")
public class ToolExecutionRequestDOController {

        private final ToolExecutionRequestDOService toolExecutionRequestDOService;

        public ToolExecutionRequestDOController(ToolExecutionRequestDOService toolExecutionRequestDOService) {
                this.toolExecutionRequestDOService = toolExecutionRequestDOService;
        }

        @PostMapping("/getRequestById")
        public ToolExecutionRequestDO getRequestById(@RequestBody String id) {
                return toolExecutionRequestDOService.getRequestById(id);
        }

        @PostMapping("/getRequestsByIds")
        public List<ToolExecutionRequestDO> getRequestsByIds(@RequestBody List<String> ids) {
                return toolExecutionRequestDOService.getRequestsByIds(ids);
        }

        @PostMapping("/getNonExecutedRequests")
        public List<ToolExecutionRequestDO> getNonExecutedRequests() {
                return toolExecutionRequestDOService.getNonExecutedRequests();
        }

}

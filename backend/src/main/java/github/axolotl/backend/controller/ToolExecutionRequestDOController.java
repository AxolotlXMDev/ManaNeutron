package github.axolotl.backend.controller;

import github.axolotl.ai.sse.ToolExecutionRequestDO;
import github.axolotl.backend.service.ToolExecutionRequestDOService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class ToolExecutionRequestDOController {

        private final ToolExecutionRequestDOService toolExecutionRequestDOService;

        public ToolExecutionRequestDOController(ToolExecutionRequestDOService toolExecutionRequestDOService) {
                this.toolExecutionRequestDOService = toolExecutionRequestDOService;
        }

        @GetMapping("/getRequestById")
        public ToolExecutionRequestDO getRequestById(@RequestParam String id) {
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

        @GetMapping("/getAll")
        public List<ToolExecutionRequestDO> getAllRequests() {
                return toolExecutionRequestDOService.getRequests();
        }

}

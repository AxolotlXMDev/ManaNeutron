package github.axolotl.backend.service;

import com.alibaba.fastjson2.JSONObject;
import dczx.axolotl.util.file.FilesUtil;
import github.axolotl.ai.session.SessionInfos;
import github.axolotl.ai.sse.ToolExecutionRequestDO;
import github.axolotl.ai.sse.ToolExecutionRequestDOs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToolExecutionRequestDOService {
        @Value("${mana-neutron.config.tool-execution-request-path}")
        private String toolExecutionRequestPath;
        private ToolExecutionRequestDOs requests;


        @PostConstruct
        public void init() throws IOException {
                FilesUtil.keepFileExists(toolExecutionRequestPath);
                String text = Files.readString(Path.of(toolExecutionRequestPath));
                requests = JSONObject.parseObject(text, ToolExecutionRequestDOs.class);
                if (requests == null || requests.getRequests() == null) {
                        requests = new ToolExecutionRequestDOs(new ArrayList<>());
                }
        }

        public void addRequest(ToolExecutionRequestDO request) {
                requests.addRequest(request);
        }

        public void removeRequest(ToolExecutionRequestDO request) {
                requests.getRequests().remove(request);
        }

        public ToolExecutionRequestDO getRequestById(String requestId) {
                return getRequests().stream()
                        .filter(request -> request.getId().equals(requestId))
                        .findFirst()
                        .orElse(null);
        }

        public List<ToolExecutionRequestDO> getRequestsByIds(List<String> requestIds) {
                List<ToolExecutionRequestDO> result = new ArrayList<>();
                for (String requestId : requestIds) {
                        ToolExecutionRequestDO request = getRequestById(requestId);
                        if (request != null) {
                                result.add(request);
                        }
                }
                return result;

        }

        public List<ToolExecutionRequestDO> getRequests() {
                return requests.getRequests();
        }

        public void saveRequests() throws IOException {
                Files.writeString(Path.of(toolExecutionRequestPath),
                        JSONObject.toJSONString(requests));
        }

}

package github.axolotl.backend.controller;

import github.axolotl.ai.sse.SSEName;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sse")
public class SSEController {

        private final Map<String, List<SseEmitter>> messageEmitter = new ConcurrentHashMap<>();//<SessionId,List<SseEmitter>>
//        private final Map<String, SseEmitter> toolRequestEmitter = new ConcurrentHashMap<>();//<SessionId,SseEmitter>

        // 消息监听
        @GetMapping(path = "/regMessageEmitter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public SseEmitter regMessageEmitter(@RequestParam String sessionId) {
                SseEmitter emitter = new SseEmitter(600_000L);

                emitter.onCompletion(() -> messageEmitter.remove(sessionId));
                emitter.onTimeout(() -> messageEmitter.remove(sessionId));

                if (!messageEmitter.containsKey(sessionId)) {
                        messageEmitter.put(sessionId, new ArrayList<>());
                }
                messageEmitter.get(sessionId).add(emitter);
                return emitter;
        }

/*        // 工具调用监听
        @GetMapping(path = "/regToolRequestEmitter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public SseEmitter regToolRequestEmitter(@RequestParam String userId) {
                SseEmitter emitter = new SseEmitter(600_000L);

                emitter.onCompletion(() -> toolRequestEmitter.remove(userId));
                emitter.onTimeout(() -> toolRequestEmitter.remove(userId));

                toolRequestEmitter.put(userId, emitter);
                return emitter;
        }*/

        public void sendEvent(String sessionId, SSEName sseName, Object data) {
                if (sseName == null) {
                        return;
                }
                List<SseEmitter> sseEmitters = this.messageEmitter.get(sessionId);
                if (sseEmitters != null) {
                        try {
                                sseEmitters.forEach(emitter -> {
                                        try {
                                                emitter.send(SseEmitter.event()
                                                        .data(data)
                                                        .name(sseName.getValueClass().getName()));
                                        } catch (IOException e) {
                                                this.messageEmitter.remove(sessionId);
                                        }
                                });
                        } catch (Exception e) {
                                this.messageEmitter.remove(sessionId);
                        }
                }
        }

}

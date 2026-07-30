package github.axolotl.ai.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionInfos {
        private List<SessionInfo> sessionInfos;

        public void addSessionInfo(SessionInfo sessionInfo) {
                this.sessionInfos.add(sessionInfo);
        }

        public void addSessionInfo(Session session) {
                addSessionInfo(session.getInfo());
        }

        public SessionInfo getSessionInfoById(String id) {
                return sessionInfos.stream()
                        .filter(session -> session.getId().equals(id))
                        .findFirst()
                        .get();
        }

        public void updateSessionInfo(SessionInfo newInfo) {
                SessionInfo oldInfo = sessionInfos.stream()
                        .filter(session -> session.getId().equals(newInfo.getId()))
                        .findFirst()
                        .get();
                sessionInfos.remove(oldInfo);
                sessionInfos.add(newInfo);
        }
}

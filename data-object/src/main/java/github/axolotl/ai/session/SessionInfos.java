package github.axolotl.ai.session;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SessionInfos {
        private List<SessionInfo> sessions;
}

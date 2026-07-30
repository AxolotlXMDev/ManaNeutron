package github.axolotl.ai.session;

import github.axolotl.setting.ModelChoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionInfo {
        private String id;
        private String workDir;
        private String name;
        private long createTime;
        private long updateTime;
        private ModelChoice modelChoice;

}

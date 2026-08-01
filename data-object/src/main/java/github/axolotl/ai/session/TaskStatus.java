package github.axolotl.ai.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatus {
        String name;
        long startWorkTime;
        long finishedTime;
        boolean isFinished;
        //TODO tokenUsageDO

        public TaskStatus(String name, long startWorkTime, boolean isFinished) {
                this.name = name;
                this.startWorkTime = startWorkTime;
                this.isFinished = isFinished;
        }
}

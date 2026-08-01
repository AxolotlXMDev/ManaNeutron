package github.axolotl.ai.session;

import github.axolotl.ai.TODO;
import github.axolotl.ai.TokenUsageDO;
import github.axolotl.ai.content.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Session {
        SessionInfo info;
        List<Content> contents;
        List<TODO> todos;
        TokenUsageDO tokenUsageDO;
        List<TaskStatus> taskStatuses;

        public String dgetId() {
                return info.getId();
        }

        public String dgetName() {
                return info.getName();
        }

        public boolean disworking() {
                if (dgetLastTaskStatus() == null) {
                        return false;
                }
                return dgetLastTaskStatus().isFinished;
        }

        public void addContent(Content content) {
                contents.add(content);
        }

        public TaskStatus dgetLastTaskStatus() {
                checkTaskStatusList();
                if (taskStatuses.isEmpty()) {
                        return null;
                }
                return taskStatuses.getLast();
        }

        public void dsetLastTaskStatus(TaskStatus task) {
                checkTaskStatusList();
                if (!taskStatuses.isEmpty()) {
                        taskStatuses.removeLast();
                }
                taskStatuses.add(task);
        }

        private void checkTaskStatusList() {
                if (taskStatuses == null) {
                        taskStatuses = new ArrayList<>();
                }
        }
}

package github.axolotl.ai.session;

import github.axolotl.ai.TODO;
import github.axolotl.ai.content.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Session {
        private SessionInfo info;
        private List<Content> contents;
        private List<TODO> todos;

        public String getId() {
                return info.getId();
        }
        public String getName() {
                return info.getName();
        }

}

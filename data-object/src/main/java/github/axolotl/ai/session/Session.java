package github.axolotl.ai.session;

import github.axolotl.ai.TODO;
import github.axolotl.ai.content.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

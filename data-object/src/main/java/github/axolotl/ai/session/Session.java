package github.axolotl.ai.session;

import github.axolotl.ai.TODO;
import github.axolotl.ai.content.Content;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Session {
        private SessionInfo info;
        private List<Content> contents;
        private List<TODO> todos;
}

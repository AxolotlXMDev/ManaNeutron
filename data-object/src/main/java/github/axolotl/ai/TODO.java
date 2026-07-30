package github.axolotl.ai;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TODO {
        private String id;
        private String task;
        private Status status;

        enum Status {
                DONE,
                WAITING,
                DOING
        }
}

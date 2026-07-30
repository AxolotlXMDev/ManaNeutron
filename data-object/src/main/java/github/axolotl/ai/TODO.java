package github.axolotl.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
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

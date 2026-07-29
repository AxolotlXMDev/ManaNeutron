package github.axolotl.ai;

import lombok.Data;

@Data
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

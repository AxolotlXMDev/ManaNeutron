package github.axolotl.ai;

import lombok.Data;

import java.util.List;

@Data
public class Session {

        private String id;
        private String workDir;
        private List<Content> contents;
        private List<TODO> todos;
}

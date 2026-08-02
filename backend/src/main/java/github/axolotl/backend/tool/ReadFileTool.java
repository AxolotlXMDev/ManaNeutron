package github.axolotl.backend.tool;

import dev.langchain4j.agent.tool.Tool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFileTool {
        @Tool
        String read_file(String filePath) {//TODO range
                try {
                        return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
                } catch (IOException e) {
                        return "Error reading %s: %s".formatted(filePath,e.getMessage());
                }
        }
}

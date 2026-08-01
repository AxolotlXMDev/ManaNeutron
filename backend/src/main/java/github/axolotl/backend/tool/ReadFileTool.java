package github.axolotl.backend.tool;

import dev.langchain4j.agent.tool.Tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFileTool {
        @Tool
        String readFile(String filePath) {//TODO range
                try {
                        return Files.readString(Path.of(filePath));
                } catch (IOException e) {
                        return "读取 {%s} 时出现了错误：%s".formatted(filePath,e.getMessage());
                }
        }
}

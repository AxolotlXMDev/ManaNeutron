package github.axolotl.ai.tool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToolParameter {
        String name;
        String description;
        Boolean required;
        Type type;

        List<String> enumValues;//仅Type = ENUM，用于存储有什么enum类型
        ToolParameter arrayType;//仅Type = ARRAY，用于存储数组的元素类型
        List<ToolParameter> properties;//仅Type = OBJECT，用于存储如何构建这个Object

        public enum Type {
                STRING,
                INTEGER,
                NUMBER,
                BOOLEAN,
                ENUM,
                ARRAY,
                OBJECT
        }
}

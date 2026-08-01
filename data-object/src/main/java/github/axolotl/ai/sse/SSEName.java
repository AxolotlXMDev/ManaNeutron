package github.axolotl.ai.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum SSEName {
        PartialResponse(PartialResponseDO.class),
        PartialThinking(PartialThinkingDO.class),
        PartialToolCall(PartialToolCallDO.class),
        ToolCallResult(ToolCallResultDO.class),
        CompleteResponse(CompleteResponseDO.class);
        @Getter
        Class<?> valueClass;
}
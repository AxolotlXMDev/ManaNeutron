package github.axolotl.ai.sse;

/**
 * Represents the token usage of a response.
 * <p>
 * from: <a href="https://github.com/langchain4j/langchain4j/blob/main/LICENSE">LICENSE</a>
 * <p>
 * Apache License
 * <p>
 * Version 2.0, January 2004
 * <p>
 * <a href="http://www.apache.org/licenses/">LICENSE</a>
 * <p>
 * The reason why a model call finished.
 */
public enum FinishReasonDO {
        /**
         * The model call finished because the model decided the request was done.
         */
        STOP,

        /**
         * The call finished because the token length was reached.
         */
        LENGTH,

        /**
         * The call finished signalling a need for tool execution.
         */
        TOOL_EXECUTION,

        /**
         * The call finished signalling a need for content filtering.
         */
        CONTENT_FILTER,

        /**
         * The call finished for some other reason.
         */
        OTHER
}


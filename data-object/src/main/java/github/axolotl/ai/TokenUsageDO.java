package github.axolotl.ai;

import java.util.Objects;

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
 */
public class TokenUsageDO {

        private final Integer inputTokenCount;
        private final Integer outputTokenCount;
        private final Integer totalTokenCount;
        //TODO inputCacheTokenCount

        /**
         * Creates a new {@link TokenUsageDO} instance with all fields set to null.
         */
        public TokenUsageDO() {
                this(null);
        }

        /**
         * Creates a new {@link TokenUsageDO} instance with the given input token count.
         *
         * @param inputTokenCount The input token count.
         */
        public TokenUsageDO(Integer inputTokenCount) {
                this(inputTokenCount, null);
        }

        /**
         * Creates a new {@link TokenUsageDO} instance with the given input and output token counts.
         *
         * @param inputTokenCount  The input token count, or null if unknown.
         * @param outputTokenCount The output token count, or null if unknown.
         */
        public TokenUsageDO(Integer inputTokenCount, Integer outputTokenCount) {
                this(inputTokenCount, outputTokenCount, sum(inputTokenCount, outputTokenCount));
        }

        /**
         * Creates a new {@link TokenUsageDO} instance with the given input, output and total token counts.
         *
         * @param inputTokenCount  The input token count, or null if unknown.
         * @param outputTokenCount The output token count, or null if unknown.
         * @param totalTokenCount  The total token count, or null if unknown.
         */
        public TokenUsageDO(Integer inputTokenCount, Integer outputTokenCount, Integer totalTokenCount) {
                this.inputTokenCount = inputTokenCount;
                this.outputTokenCount = outputTokenCount;
                this.totalTokenCount = totalTokenCount;
        }

        /**
         * Returns the input token count, or null if unknown.
         *
         * @return the input token count, or null if unknown.
         */
        public Integer inputTokenCount() {
                return inputTokenCount;
        }

        /**
         * Returns the output token count, or null if unknown.
         *
         * @return the output token count, or null if unknown.
         */
        public Integer outputTokenCount() {
                return outputTokenCount;
        }

        /**
         * Returns the total token count, or null if unknown.
         *
         * @return the total token count, or null if unknown.
         */
        public Integer totalTokenCount() {
                return totalTokenCount;
        }

        /**
         * Adds two token usages.
         * <br>
         * If one of the token usages is null, the other is returned without changes.
         * <br>
         * Fields which are null in both responses will be null in the result.
         *
         * @param first  The first token usage to add.
         * @param second The second token usage to add.
         * @return a new {@link TokenUsageDO} instance with the sum of token usages.
         */
        public static TokenUsageDO sum(TokenUsageDO first, TokenUsageDO second) {
                if (first == null) {
                        return second;
                } else if (second == null) {
                        return first;
                } else {
                        return first.add(second);
                }
        }

        /**
         * Adds the token usage of two responses together.
         *
         * <p>Fields which are null in both responses will be null in the result.
         *
         * @param that The token usage to add to this one.
         * @return a new {@link TokenUsageDO} instance with the token usage of both responses added together.
         */
        public TokenUsageDO add(TokenUsageDO that) {
                if (that == null) {
                        return this;
                }

                if (that.getClass() != TokenUsageDO.class) {
                        // when adding TokenUsage ("this") and one of TokenUsage's subclasses ("that"),
                        // we want to call "add" on the subclass to preserve extra information present in the subclass
                        return that.add(this);
                }

                return new TokenUsageDO(
                        sum(this.inputTokenCount, that.inputTokenCount),
                        sum(this.outputTokenCount, that.outputTokenCount),
                        sum(this.totalTokenCount, that.totalTokenCount)
                );
        }

        /**
         * Sum two integers, returning null if both are null.
         *
         * @param first  The first integer, or null.
         * @param second The second integer, or null.
         * @return the sum of the two integers, or null if both are null.
         */
        protected static Integer sum(Integer first, Integer second) {
                if (first == null && second == null) {
                        return null;
                }

                return getOrDefault(first, 0) + getOrDefault(second, 0);
        }

        /**
         * Returns the given value if it is not {@code null}, otherwise returns the given default value.
         *
         * @param value        The value to return if it is not {@code null}.
         * @param defaultValue The value to return if the value is {@code null}.
         * @param <T>          The type of the value.
         * @return the given value if it is not {@code null}, otherwise returns the given default value.
         */
        public static <T> T getOrDefault(T value, T defaultValue) {
                return value != null ? value : defaultValue;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TokenUsageDO that = (TokenUsageDO) o;
                return Objects.equals(this.inputTokenCount, that.inputTokenCount)
                        && Objects.equals(this.outputTokenCount, that.outputTokenCount)
                        && Objects.equals(this.totalTokenCount, that.totalTokenCount);
        }

        @Override
        public int hashCode() {
                return Objects.hash(inputTokenCount, outputTokenCount, totalTokenCount);
        }

        @Override
        public String toString() {
                return "TokenUsage {" +
                        " inputTokenCount = " + inputTokenCount +
                        ", outputTokenCount = " + outputTokenCount +
                        ", totalTokenCount = " + totalTokenCount +
                        " }";
        }
}

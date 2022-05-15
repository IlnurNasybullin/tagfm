package io.github.ilnurnasybullin.tagfm.core.search;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface LogicalExpressionParser<T> {
    LogicalExpressionEvaluator<T> parse(List<String> tokens, Function<String, T> mapper);

    @SuppressWarnings("unchecked")
    static <T> LogicalExpressionParser<T> get() {
        return ServiceLoader.load(LogicalExpressionParser.class)
                .findFirst()
                .orElseThrow();
    }
}

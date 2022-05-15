package io.github.ilnurnasybullin.tagfm.core.search;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public interface LogicalExpressionEvaluator<T> {
    boolean evaluate(Predicate<T> mapper);
    <U> LogicalExpressionEvaluator<U> map(Function<T, U> mapper);
}

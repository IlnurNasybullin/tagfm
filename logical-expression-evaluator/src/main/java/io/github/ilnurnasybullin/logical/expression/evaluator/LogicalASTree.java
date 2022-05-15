package io.github.ilnurnasybullin.logical.expression.evaluator;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public interface LogicalASTree<T> {
    <U> LogicalASTree<U> map(Function<T, U> mapFunction);
    boolean evaluate(Predicate<T> mapper);
    int leafsCount();
}

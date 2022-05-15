package io.github.ilnurnasybullin.tagfm.core.search;

import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
@FunctionalInterface
public interface LogicalExpressionComputer<T> {
    boolean compute(Predicate<T> mapper);
}

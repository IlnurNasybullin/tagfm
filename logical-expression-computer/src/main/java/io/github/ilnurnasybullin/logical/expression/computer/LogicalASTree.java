package io.github.ilnurnasybullin.logical.expression.computer;

import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public interface LogicalASTree<T> {
    boolean compute(Predicate<T> mapper);
}

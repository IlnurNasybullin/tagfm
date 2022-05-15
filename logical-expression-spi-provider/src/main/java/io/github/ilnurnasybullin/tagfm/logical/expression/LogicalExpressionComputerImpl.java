package io.github.ilnurnasybullin.tagfm.logical.expression;

import io.github.ilnurnasybullin.logical.expression.computer.LogicalASTree;
import io.github.ilnurnasybullin.tagfm.core.search.LogicalExpressionComputer;

import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public class LogicalExpressionComputerImpl<T> implements LogicalExpressionComputer<T> {

    private final LogicalASTree<T> computerTree;

    public LogicalExpressionComputerImpl(LogicalASTree<T> computerTree) {
        this.computerTree = computerTree;
    }

    @Override
    public boolean compute(Predicate<T> mapper) {
        return computerTree.compute(mapper);
    }
}

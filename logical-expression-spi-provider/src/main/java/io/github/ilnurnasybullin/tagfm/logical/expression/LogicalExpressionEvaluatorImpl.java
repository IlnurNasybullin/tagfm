package io.github.ilnurnasybullin.tagfm.logical.expression;

import io.github.ilnurnasybullin.logical.expression.evaluator.LogicalASTree;
import io.github.ilnurnasybullin.tagfm.core.search.LogicalExpressionEvaluator;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ilnur Nasybullin
 */
public class LogicalExpressionEvaluatorImpl<T> implements LogicalExpressionEvaluator<T> {

    private final LogicalASTree<T> evaluatorTree;

    public LogicalExpressionEvaluatorImpl(LogicalASTree<T> evaluatorTree) {
        this.evaluatorTree = evaluatorTree;
    }

    @Override
    public boolean evaluate(Predicate<T> mapper) {
        return evaluatorTree.evaluate(mapper);
    }

    @Override
    public <U> LogicalExpressionEvaluator<U> map(Function<T, U> mapper) {
        return new LogicalExpressionEvaluatorImpl<>(evaluatorTree.map(mapper));
    }
}

package io.github.ilnurnasybullin.tagfm.logical.expression;

import io.github.ilnurnasybullin.logical.expression.evaluator.LogicalASTree;
import io.github.ilnurnasybullin.tagfm.core.search.LogicalExpressionEvaluator;
import io.github.ilnurnasybullin.tagfm.core.search.LogicalExpressionParser;

import java.util.List;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
public class LogicalExpressionParserImpl<T> implements LogicalExpressionParser<T> {

    @Override
    public LogicalExpressionEvaluator<T> parse(List<String> tokens, Function<String, T> mapper) {
        LogicalASTree<T> evaluatorTree = getParser(mapper)
                .parse(tokens);
        return new LogicalExpressionEvaluatorImpl<>(evaluatorTree);
    }

    private io.github.ilnurnasybullin.logical.expression.parser.LogicalExpressionParser<T> getParser(Function<String, T> mapper) {
        return new io.github.ilnurnasybullin.logical.expression.parser.LogicalExpressionParser<>(mapper);
    }
}

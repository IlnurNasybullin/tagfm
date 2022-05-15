import io.github.ilnurnasybullin.tagfm.core.search.LogicalExpressionParser;
import io.github.ilnurnasybullin.tagfm.logical.expression.LogicalExpressionParserImpl;

/**
 * @author Ilnur Nasybullin
 */
module tagfm.logical.expression.spi.provider {
    requires tagfm.core;
    requires logical.expression.computer;

    exports io.github.ilnurnasybullin.tagfm.logical.expression;

    provides LogicalExpressionParser with LogicalExpressionParserImpl;
}
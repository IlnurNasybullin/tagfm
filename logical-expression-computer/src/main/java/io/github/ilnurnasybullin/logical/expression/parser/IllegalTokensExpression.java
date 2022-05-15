package io.github.ilnurnasybullin.logical.expression.parser;

/**
 * @author Ilnur Nasybullin
 */
public class IllegalTokensExpression extends IllegalStateException {

    public IllegalTokensExpression() {
    }

    public IllegalTokensExpression(String s) {
        super(s);
    }

    public IllegalTokensExpression(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTokensExpression(Throwable cause) {
        super(cause);
    }
}

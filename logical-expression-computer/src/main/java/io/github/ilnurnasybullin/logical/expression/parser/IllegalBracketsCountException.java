package io.github.ilnurnasybullin.logical.expression.parser;

/**
 * @author Ilnur Nasybullin
 */
public class IllegalBracketsCountException extends IllegalStateException {
    public IllegalBracketsCountException() {
    }

    public IllegalBracketsCountException(String s) {
        super(s);
    }

    public IllegalBracketsCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalBracketsCountException(Throwable cause) {
        super(cause);
    }
}

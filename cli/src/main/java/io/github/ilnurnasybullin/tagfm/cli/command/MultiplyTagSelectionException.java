package io.github.ilnurnasybullin.tagfm.cli.command;

public class MultiplyTagSelectionException extends IllegalArgumentException {
    public MultiplyTagSelectionException() {
    }

    public MultiplyTagSelectionException(String s) {
        super(s);
    }

    public MultiplyTagSelectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultiplyTagSelectionException(Throwable cause) {
        super(cause);
    }
}

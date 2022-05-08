package io.github.ilnurnasybullin.tagfm.cli.command;

public class NamespaceNotInitializedException extends IllegalStateException {
    public NamespaceNotInitializedException() {
    }

    public NamespaceNotInitializedException(String s) {
        super(s);
    }

    public NamespaceNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceNotInitializedException(Throwable cause) {
        super(cause);
    }
}

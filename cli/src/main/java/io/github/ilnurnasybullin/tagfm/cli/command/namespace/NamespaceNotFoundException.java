package io.github.ilnurnasybullin.tagfm.cli.command.namespace;

public class NamespaceNotFoundException extends IllegalStateException {
    public NamespaceNotFoundException() {
    }

    public NamespaceNotFoundException(String s) {
        super(s);
    }

    public NamespaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceNotFoundException(Throwable cause) {
        super(cause);
    }
}

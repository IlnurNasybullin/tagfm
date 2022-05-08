package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

public class NamespaceAlreadyInitialized extends IllegalStateException {
    public NamespaceAlreadyInitialized() {
    }

    public NamespaceAlreadyInitialized(String s) {
        super(s);
    }

    public NamespaceAlreadyInitialized(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceAlreadyInitialized(Throwable cause) {
        super(cause);
    }
}

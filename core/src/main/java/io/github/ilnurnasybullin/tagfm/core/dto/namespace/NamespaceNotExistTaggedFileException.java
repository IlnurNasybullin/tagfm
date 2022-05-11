package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

public class NamespaceNotExistTaggedFileException extends IllegalStateException {
    public NamespaceNotExistTaggedFileException() {
    }

    public NamespaceNotExistTaggedFileException(String s) {
        super(s);
    }

    public NamespaceNotExistTaggedFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceNotExistTaggedFileException(Throwable cause) {
        super(cause);
    }
}

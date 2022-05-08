package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

public class InvalidNamespaceNameException extends IllegalArgumentException {
    public InvalidNamespaceNameException() {
    }

    public InvalidNamespaceNameException(String s) {
        super(s);
    }

    public InvalidNamespaceNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNamespaceNameException(Throwable cause) {
        super(cause);
    }
}

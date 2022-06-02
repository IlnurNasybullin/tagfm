package io.github.ilnurnasybullin.tagfm.repository.xml.repository;

public class NamespaceNotExistingException extends IllegalStateException {
    public NamespaceNotExistingException() {
    }

    public NamespaceNotExistingException(String message) {
        super(message);
    }

    public NamespaceNotExistingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceNotExistingException(Throwable cause) {
        super(cause);
    }
}

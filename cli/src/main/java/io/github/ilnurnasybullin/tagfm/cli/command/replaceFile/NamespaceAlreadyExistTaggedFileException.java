package io.github.ilnurnasybullin.tagfm.cli.command.replaceFile;

/**
 * @author Ilnur Nasybullin
 */
public class NamespaceAlreadyExistTaggedFileException extends IllegalStateException {
    public NamespaceAlreadyExistTaggedFileException() {
    }

    public NamespaceAlreadyExistTaggedFileException(String s) {
        super(s);
    }

    public NamespaceAlreadyExistTaggedFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamespaceAlreadyExistTaggedFileException(Throwable cause) {
        super(cause);
    }
}

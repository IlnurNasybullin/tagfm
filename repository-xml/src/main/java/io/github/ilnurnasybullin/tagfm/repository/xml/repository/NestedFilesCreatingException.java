package io.github.ilnurnasybullin.tagfm.repository.xml.repository;

import java.io.IOException;
import java.io.UncheckedIOException;

public class NestedFilesCreatingException extends UncheckedIOException {
    public NestedFilesCreatingException(String message, IOException cause) {
        super(message, cause);
    }

    public NestedFilesCreatingException(IOException cause) {
        super(cause);
    }
}

package io.github.ilnurnasybullin.tagfm.core.dto.file;

import java.io.IOException;
import java.io.UncheckedIOException;

public class UnexistingFileException extends UncheckedIOException {
    public UnexistingFileException(String message, IOException cause) {
        super(message, cause);
    }

    public UnexistingFileException(IOException cause) {
        super(cause);
    }

    public UnexistingFileException(String message) {
        super(message, new IOException());
    }
}

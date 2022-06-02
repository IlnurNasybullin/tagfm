package io.github.ilnurnasybullin.tagfm.repository.xml.fileManager;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.Callable;

public class FilesTransactionManager {

    public void transactionCreate(Path file, Callable<TransactionState> action) {
        Optional<Path> createdFile = createIfNotExist(file);
        TransactionState state = TransactionState.REJECTED;
        try {
            state = action.call();
        } catch (Exception e) {
            throw new TransactionException(e);
        } finally {
            if (state == TransactionState.REJECTED) {
                createdFile.ifPresent(this::removeIfExist);
            }
        }
    }

    private Optional<Path> createIfNotExist(Path file) {
        try {
            return Files.notExists(file) ? Optional.of(Files.createFile(file)) : Optional.empty();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void removeIfExist(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

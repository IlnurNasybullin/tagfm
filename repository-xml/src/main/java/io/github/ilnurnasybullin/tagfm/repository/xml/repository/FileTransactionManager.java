package io.github.ilnurnasybullin.tagfm.repository.xml.repository;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class FileTransactionManager implements AutoCloseable {

    private final Path savingFile;
    private boolean isCreated;
    private boolean isSuccess;

    public FileTransactionManager(Path savingFile) {
        this.savingFile = savingFile;
    }

    private void initPath() {
        if (Files.notExists(savingFile)) {
            createFiles(savingFile);
            isCreated = true;
        }
    }

    private void removePath() {
        try {
            Files.walk(savingFile.getParent())
                    .filter(Files::exists)
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void createFiles(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (IOException e) {
            throw new NestedFilesCreatingException(
                    String.format("Problems with creating nested paths of [%s]", path), e
            );
        }
    }

    public void acknowledge() {
        isSuccess = true;
    }

    @Override
    public void close() {
        if (!isSuccess && isCreated) {
            removePath();
        }
    }

}

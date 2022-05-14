package io.github.ilnurnasybullin.tagfm.cli.command.list;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceNotExistTaggedFileException;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "file-tags")
public class ListFileTagsCommand implements Runnable {

    @CommandLine.Parameters(arity = "1", index = "0")
    private Path file;

    private final FileManagerCommand fileManager;

    public ListFileTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        Path realPath;
        try {
            realPath = file.toAbsolutePath().toRealPath();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        TaggedFileDto searchedFile = namespace.files()
                .stream()
                .filter(taggedFile -> Objects.equals(taggedFile.file(), realPath))
                .findAny()
                .orElseThrow(() -> new NamespaceNotExistTaggedFileException(
                        String.format("File [%s] isn't exist in namespace [%s]!", realPath, namespace.name())
                ));

        searchedFile.tags()
                .stream()
                .map(TreeTagDto::fullName)
                .forEach(System.out::println);
    }
}

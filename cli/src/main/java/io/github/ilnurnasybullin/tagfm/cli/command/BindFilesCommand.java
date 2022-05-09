package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceFileManager;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Singleton
@CommandLine.Command(name = "files")
public class BindFilesCommand implements Runnable {

    @CommandLine.Parameters(arity = "1", split = ",")
    private Path[] files;

    @CommandLine.Option(names = {"-t", "--tags"}, required = true, arity = "1", split = ",")
    private String[] tags;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    private final FileManagerCommand fileManager;

    public BindFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        List<TreeTagDto> tags = getTags(namespace);

        getFiles(namespace).forEach(taggedFile -> {
            taggedFile.tags().addAll(tags);
            namespace.files().add(taggedFile);
        });
        fileManager.setWriteMode();
    }

    private Stream<TaggedFileDto> getFiles(NamespaceDto namespace) {
        NamespaceFileManager namespaceFileManager = new NamespaceFileManager();
        return Arrays.stream(files).map(file -> {
            try {
                return namespaceFileManager.findOrCreate(file, namespace);
            } catch (IOException e) {
                throw new UncheckedIOException(String.format("Problems with file [%s]", file), e);
            }
        });
    }

    private List<TreeTagDto> getTags(NamespaceDto namespace) {
        NamespaceTagSearcherFacade tagSearcher = new NamespaceTagSearcherFacade();
        return Arrays.stream(this.tags)
                .map(tagName -> tagSearcher.searchTag(tagName, namespace, shortName))
                .toList();
    }
}

package io.github.ilnurnasybullin.tagfm.cli.command.unbind;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.FileRemovingPolicy;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Singleton
@CommandLine.Command(name = "file-tags")
public class UnbindFileTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1")
    private final List<Path> files = new ArrayList<>();

    @CommandLine.Option(names = {"-t", "--tags"})
    private final List<String> tags = new ArrayList<>();

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-frp", "--file-removing-policy"}, paramLabel = "file removing policy")
    private FileRemovingPolicy removingPolicy = FileRemovingPolicy.NO_REMOVE;

    public UnbindFileTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        Collection<TreeTagDto> tags = getTags(namespace);
        getFiles(namespace).forEach(file -> tags.forEach(file.tags()::remove));

        if (removingPolicy == FileRemovingPolicy.REMOVE_IF_NO_TAGS) {
            namespace.files().removeIf(file -> file.tags().isEmpty());
        }

        fileManager.setWriteMode();
    }

    private List<TreeTagDto> getTags(NamespaceDto namespace) {
        return tags.isEmpty() ?
                List.of() : new NamespaceTagSearcherFacade()
                .searchTags(tags, namespace, shortName)
                .toList();
    }

    private Stream<TaggedFileDto> getFiles(NamespaceDto namespace) {
        return new NamespaceFileManagerFacade()
                .find(files, namespace);
    }
}

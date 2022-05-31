package io.github.ilnurnasybullin.tagfm.cli.command.tag.bind;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.api.service.NamespaceTagAdder;

import java.nio.file.Path;

import jakarta.inject.Singleton;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Singleton
@Command(name = "files")
public class BindFilesCommand implements Runnable {
    
    @Parameters(index = "0", arity = "1")
    private String tagName;

    @Parameters(index = "1..*", arity = "1")
    private final List<Path> files = new ArrayList<>();

    @Option(names = {"-c", "--create-tag"})
    private boolean createTag;

    @Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    private final FileManagerCommand fileManager;

    public BindFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();
        Tag tag = getTag(namespace);
        getFiles(namespace).forEach(taggedFile -> {
            taggedFile.tags().add(tag);
            namespace.files().add(taggedFile);
        });

        fileManager.setWriteMode();
    }

    private Tag getTag(Namespace namespace) {
        NamespaceTagSearcherFacade facade = new NamespaceTagSearcherFacade();
        Tag tag = facade.searchOrCreate(tagName, namespace, shortName);
        NamespaceTagAdder.of(namespace).addTag(tag);
        return tag;
    }

    private Stream<TaggedFile> getFiles(Namespace namespace) {
        NamespaceFileManagerFacade facade = new NamespaceFileManagerFacade();
        return facade.findOrCreate(files, namespace);
    }

}

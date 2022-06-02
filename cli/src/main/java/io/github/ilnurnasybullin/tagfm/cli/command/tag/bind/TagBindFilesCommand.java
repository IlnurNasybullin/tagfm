package io.github.ilnurnasybullin.tagfm.cli.command.tag.bind;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileFinderManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.NamespaceTagAdder;

import java.nio.file.Path;

import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Singleton
@Command(name = "files")
public class TagBindFilesCommand implements Runnable {
    
    @Parameters(index = "0", arity = "1")
    private String tagName;

    @Parameters(index = "1..*", arity = "1")
    private final List<Path> files = new ArrayList<>();

    @Option(names = {"-c", "--create-tag"})
    private boolean createTag;

    @Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    private final FileManagerCommand fileManager;

    public TagBindFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TagView tag = getTag(namespace);
        getFiles(namespace).forEach(taggedFile -> {
            taggedFile.tags().add(tag);
            namespace.files().add(taggedFile);
        });

        fileManager.commit();
    }

    private TagView getTag(NamespaceView namespace) {
        TagService tagService = TagService.of(namespace);
        if (!createTag) {
            return shortName ?
                    tagService.findByNameExact(tagName) :
                    tagService.findByFullNameExact(tagName);
        }

        TagView tag = shortName ?
                tagService.findOrCreateByNameExact(tagName) :
                tagService.findOrCreateByFullName(tagName);

        NamespaceTagAdder.of(namespace).addTag(tag);
        return tag;
    }

    private Stream<TaggedFileView> getFiles(NamespaceView namespace) {
        return FileFinderManager.of(namespace)
                .findOrCreate(files)
                .values()
                .stream();
    }

}

package io.github.ilnurnasybullin.tagfm.cli.command.addFiles;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceTagAdder;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagCreator;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@CommandLine.Command(name = "add-tags")
public class AddTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(index = "*", paramLabel = "tags", arity = "1..*")
    private final List<String> tags = new ArrayList<>();

    public AddTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();

        TreeTagCreator creator = new TreeTagCreator();
        List<TreeTagDto> treeTags = tags.stream()
                .map(creator::deepCreate)
                .flatMap(Optional::stream)
                .toList();

        NamespaceTagAdder.of(namespace).addTags(treeTags);
        fileManager.setWriteMode();
    }
}

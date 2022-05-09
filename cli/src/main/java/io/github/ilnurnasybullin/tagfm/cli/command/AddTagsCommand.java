package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceTagsAdder;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagCreator;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Singleton
@CommandLine.Command(name = "add-tags")
public class AddTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    private final Pattern pattern = Pattern.compile(",\\s+");

    @CommandLine.Parameters(index = "*", paramLabel = "tags", arity = "1..*", split = ",")
    private String[] tagNames;

    public AddTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }


    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();

        TreeTagCreator creator = new TreeTagCreator();
        List<TreeTagDto> tags = Arrays.stream(tagNames)
                .map(creator::deepCreate)
                .flatMap(Optional::stream)
                .toList();

        new NamespaceTagsAdder().addTags(tags, namespace);
        fileManager.setWriteMode();
    }
}
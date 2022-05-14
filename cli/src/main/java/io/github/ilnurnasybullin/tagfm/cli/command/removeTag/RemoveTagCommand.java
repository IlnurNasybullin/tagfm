package io.github.ilnurnasybullin.tagfm.cli.command.removeTag;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceTagRemover;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.TagRemovingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "remove-tag")
public class RemoveTagCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-trs", "--tag-removing-strategy"})
    private TagRemovingStrategy tagRemovingStrategy = TagRemovingStrategy.UP_CHILDREN_WITHOUT_CONFLICTS;

    @CommandLine.Parameters(index = "0", arity = "1")
    private String tagName;

    public RemoveTagCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        TreeTagDto searchedTag = searchTag(tagName, namespace);
        NamespaceTagRemover.of(namespace).removeTag(searchedTag, tagRemovingStrategy);
        fileManager.setWriteMode();
    }

    private TreeTagDto searchTag(String name, NamespaceDto namespace) {
        return new NamespaceTagSearcherFacade().searchTag(name, namespace, shortName);
    }
}

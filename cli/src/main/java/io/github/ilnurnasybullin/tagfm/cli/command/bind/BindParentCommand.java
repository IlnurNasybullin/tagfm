package io.github.ilnurnasybullin.tagfm.cli.command.bind;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.TagParentBindingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TagParentBinding;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "parent")
public class BindParentCommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1")
    private String parentTag;

    @CommandLine.Parameters(index = "1", arity = "1")
    private String childTag;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-pbs", "--parent-binding-strategy"})
    private TagParentBindingStrategy parentBindingStrategy = TagParentBindingStrategy.THROW_IF_COLLISION;

    private final FileManagerCommand fileManager;

    public BindParentCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        TreeTagDto parentTag = getTag(namespace, this.parentTag);
        TreeTagDto childTag = getTag(namespace, this.childTag);

        TagParentBinding.of(namespace).bindParent(childTag, parentTag, parentBindingStrategy);
        fileManager.setWriteMode();
    }

    private TreeTagDto getTag(NamespaceDto namespace, String tagName) {
        NamespaceTagSearcherFacade facade = new NamespaceTagSearcherFacade();
        return facade.searchTag(tagName, namespace, shortName);
    }
}

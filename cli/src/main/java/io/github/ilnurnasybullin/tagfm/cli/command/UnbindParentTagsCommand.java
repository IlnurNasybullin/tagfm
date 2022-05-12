package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.TagParentBindingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TagParentBinding;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "parent-tag")
public class UnbindParentTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-pbs", "--parent-binding-strategy"})
    private TagParentBindingStrategy parentBindingStrategy = TagParentBindingStrategy.THROW_IF_COLLISION;

    @CommandLine.Parameters(arity = "1")
    private String childTag;

    public UnbindParentTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        TreeTagDto tag = new NamespaceTagSearcherFacade().searchTag(childTag, namespace, shortName);
        TagParentBinding.of(namespace).unbind(tag, parentBindingStrategy);
        fileManager.setWriteMode();
    }
}

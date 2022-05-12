package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.TagParentBindingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TagParentBinding;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@CommandLine.Command(name = "parent")
public class BindParentCommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1")
    private String parentTag;

    @CommandLine.Parameters(index = "1", split = ",")
    private final List<String> childTags = new ArrayList<>();

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
        List<TreeTagDto> tags = getTags(namespace);

        TreeTagDto parentTag = tags.remove(tags.size() - 1);
        TagParentBinding binding = TagParentBinding.of(namespace);
        tags.forEach(tag -> binding.bindParent(tag, parentTag, parentBindingStrategy));

        fileManager.setWriteMode();
    }

    private List<TreeTagDto> getTags(NamespaceDto namespace) {
        NamespaceTagSearcherFacade facade = new NamespaceTagSearcherFacade();
        childTags.add(parentTag);
        return facade
                .searchTags(childTags, namespace, shortName)
                .collect(Collectors.toList());
    }
}

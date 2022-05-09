package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.TreeTagSearcher;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "rename-tag")
public class RenameTagCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName = false;

    @CommandLine.Parameters(index = "0", arity = "1")
    private String oldName;

    @CommandLine.Parameters(index = "1", arity = "1")
    private String newName;

    public RenameTagCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        TreeTagDto searchedTag = searchTag(oldName, namespace);
        searchedTag.rename(newName);
        fileManager.setWriteMode();
    }

    private TreeTagDto searchTag(String name, NamespaceDto namespace) {
        TreeTagSearcher tagSearcher = new TreeTagSearcher();

        if (!shortName) {
            return tagSearcher.findByFullName(oldName, namespace).orElseThrow(() ->
                    tagNotFound(name, namespace)
            );
        }

        TreeTagDto[] tags = tagSearcher.findByName(name, namespace).toArray(TreeTagDto[]::new);
        if (tags.length == 0) {
            throw tagNotFound(name, namespace);
        }
        if (tags.length > 1) {
            throw new MultiplyTagSelectionException(String.format(
                    "Tag with short name [%s] is not unique in namespace [%s]!",
                    name, namespace.name()
            ));
        }

        return tags[0];
    }

    private IllegalArgumentException tagNotFound(String name, NamespaceDto namespace) {
        return new IllegalArgumentException(String.format(
                "Tag with name [%s] is not found in namespace [%s]!",
                name, namespace.name()
        ));
    }
}

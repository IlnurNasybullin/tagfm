package io.github.ilnurnasybullin.tagfm.cli.command.renameTag;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "rename-tag")
public class RenameTagCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName = false;

    @CommandLine.Parameters(index = "0")
    private String oldName;

    @CommandLine.Parameters(index = "1")
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
        return new NamespaceTagSearcherFacade().searchTag(name, namespace, shortName);
    }
}

package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@CommandLine.Command(name = "rename")
@Singleton
public class NamespaceRenameCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1")
    private String newName;

    public NamespaceRenameCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto renamedNamespace = fileManager.namespaceOrThrow()
                .rename(newName);
        fileManager.setNamespace(renamedNamespace);
        fileManager.setWriteMode();
    }
}

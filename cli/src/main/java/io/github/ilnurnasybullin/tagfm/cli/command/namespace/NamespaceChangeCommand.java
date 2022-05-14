package io.github.ilnurnasybullin.tagfm.cli.command.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@CommandLine.Command(name = "change")
@Singleton
public class NamespaceChangeCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-fns", "--file-naming-strategy"})
    private FileNamingStrategy strategy;

    public NamespaceChangeCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        if (strategy != null) {
            NamespaceDto namespace = fileManager.namespaceOrThrow();
            if (namespace.fileNaming() != strategy) {
                namespace.replaceFileNamingStrategy(strategy);
                fileManager.setWriteMode();
            }
        }
    }
}

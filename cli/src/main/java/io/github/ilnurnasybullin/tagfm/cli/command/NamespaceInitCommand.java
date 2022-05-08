package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceServiceImpl;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Optional;

@CommandLine.Command(name = "init")
@Singleton
public class NamespaceInitCommand implements Runnable {

    private final FileManagerCommand fileManager;
    private final NamespaceServiceImpl namespaceService;

    @CommandLine.Option(names = {"-fns", "--file-naming-strategy"})
    private FileNamingStrategy fileNaming = FileNamingStrategy.RELATIVE;

    @CommandLine.Parameters
    private String name;

    public NamespaceInitCommand(FileManagerCommand fileManager, NamespaceServiceImpl namespaceService) {
        this.fileManager = fileManager;
        this.namespaceService = namespaceService;
    }

    @Override
    public void run() {
        fileManager.checkNamespaceOnNonExisting();
        fileManager.setWriteMode();
        fileManager.initNamespace(Optional.of(namespaceService.init(name, fileNaming)));
    }
}

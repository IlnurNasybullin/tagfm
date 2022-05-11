package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Singleton
@CommandLine.Command(name = "files")
public class UnbindFilesCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", split = ",")
    private List<Path> files;

    public UnbindFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        List<TaggedFileDto> files = getFiles(namespace);
        Set<TaggedFileDto> namespaceFiles = namespace.files();
        files.forEach(namespaceFiles::remove);
        fileManager.setWriteMode();
    }

    private List<TaggedFileDto> getFiles(NamespaceDto namespace) {
        return new NamespaceFileManagerFacade()
                .find(files, namespace)
                .toList();
    }
}

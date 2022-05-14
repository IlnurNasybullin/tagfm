package io.github.ilnurnasybullin.tagfm.cli.command.replaceFile;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "replace-file")
public class ReplaceFileCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0")
    private Path src;

    @CommandLine.Parameters(arity = "1", index = "1")
    private Path dest;

    public ReplaceFileCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();

        NamespaceFileManagerFacade facade = new NamespaceFileManagerFacade();
        TaggedFileDto taggedFile = facade.findExact(src, namespace);
        facade.find(dest, namespace).ifPresent(file -> {
            throw new NamespaceAlreadyExistTaggedFileException(
                    String.format("File or dir [%s] is already existed in namespace [%s]!", file, namespace.name())
            );
        });

        taggedFile.replace(dest);
        fileManager.setWriteMode();
    }
}

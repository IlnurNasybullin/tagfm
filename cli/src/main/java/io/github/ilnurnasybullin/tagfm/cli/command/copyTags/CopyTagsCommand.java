package io.github.ilnurnasybullin.tagfm.cli.command.copyTags;

import io.github.ilnurnasybullin.tagfm.cli.command.CopyTagsStrategy;
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
@CommandLine.Command(name = "copy-tags")
public class CopyTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0")
    private Path src;

    @CommandLine.Parameters(arity = "1", index = "1")
    private Path dest;

    @CommandLine.Option(names = {"-c", "--create-dest"}, arity = "0")
    private boolean createIfNotExist;

    @CommandLine.Option(names = {"-cts", "--copy-tags-strategy"})
    private CopyTagsStrategy copyTagsStrategy = CopyTagsStrategy.ADD;

    public CopyTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();

        NamespaceFileManagerFacade facade = new NamespaceFileManagerFacade();
        TaggedFileDto srcTaggedFile = facade.findExact(src, namespace);

        TaggedFileDto destTaggedFile = createIfNotExist ?
                facade.findOrCreate(dest, namespace) :
                facade.findExact(dest, namespace);

        namespace.files().add(destTaggedFile);

        if (copyTagsStrategy == CopyTagsStrategy.REPLACE) {
            destTaggedFile.tags().clear();
        }

        destTaggedFile.tags().addAll(srcTaggedFile.tags());

        fileManager.setWriteMode();
    }
}

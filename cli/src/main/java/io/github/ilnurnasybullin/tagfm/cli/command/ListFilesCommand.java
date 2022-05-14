package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "files")
public class ListFilesCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-fns", "--file-naming-strategy"})
    private FileNamingStrategy strategy = FileNamingStrategy.ABSOLUTE;

    public ListFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Path currentPath = Path.of(".").toAbsolutePath();

        UnaryOperator<Path> fileMapping = strategy ==
                FileNamingStrategy.ABSOLUTE ?
                UnaryOperator.identity() :
                currentPath::relativize;

        fileManager.namespaceOrThrow()
                .files()
                .stream()
                .map(TaggedFileDto::file)
                .map(fileMapping)
                .sorted()
                .forEach(System.out::println);
    }
}

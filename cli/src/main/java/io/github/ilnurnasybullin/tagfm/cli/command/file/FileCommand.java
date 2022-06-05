package io.github.ilnurnasybullin.tagfm.cli.command.file;

import io.github.ilnurnasybullin.tagfm.cli.command.file.merge.CopyCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.file.unbind.RemoveFilesCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.file.unbind.UnbindFileTagsCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.ReusableOption;
import jakarta.inject.Singleton;
import picocli.CommandLine;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "file",
        subcommands = {
            FileTagBindsCommand.class,
            CopyCommand.class,
            UnbindFileTagsCommand.class,
            FileReplaceCommand.class,
            FileSearchCommand.class,
            RemoveFilesCommand.class
        },
        description = """
                binding/unbinding tags to file, removing file from namespace, replacing file and search files by tags \
                using logical operators (&, |, ~).
                """
)
public class FileCommand {

    @CommandLine.Mixin
    private ReusableOption options;

}


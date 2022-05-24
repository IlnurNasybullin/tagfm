package io.github.ilnurnasybullin.tagfm.cli.command.tag;

import io.github.ilnurnasybullin.tagfm.cli.command.option.ReusableOption;
import io.github.ilnurnasybullin.tagfm.cli.command.tag.bind.TagBindCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.tag.unbind.TagUnbindCommand;
import jakarta.inject.Singleton;
import picocli.CommandLine;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "tag",
        subcommands = {
            TagAddCommand.class,
            TagRenameCommand.class,
            TagRemoveCommand.class,
            TagBindCommand.class,
            TagUnbindCommand.class
        },
        description = """
                managing with tags (add/edit/remove tags from namespace), binding/unbinding parent tag or synonyms
                """
)
public class TagCommand implements Runnable {

    @CommandLine.Mixin
    private ReusableOption option;

    @Override
    public void run() {

    }
}

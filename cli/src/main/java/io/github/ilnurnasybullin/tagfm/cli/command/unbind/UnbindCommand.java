package io.github.ilnurnasybullin.tagfm.cli.command.unbind;

import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "unbind", subcommands = {
        UnbindFileTagsCommand.class,
        UnbindFilesCommand.class,
        UnbindSynonymsCommand.class,
        UnbindParentTagsCommand.class
})
public class UnbindCommand implements Runnable {
    @Override
    public void run() {

    }
}

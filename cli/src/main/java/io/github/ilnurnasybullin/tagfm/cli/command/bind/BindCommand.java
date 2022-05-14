package io.github.ilnurnasybullin.tagfm.cli.command.bind;

import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "bind", subcommands = {
        BindFilesCommand.class,
        BindSynonymsCommand.class,
        BindParentCommand.class
})
public class BindCommand implements Runnable {
    @Override
    public void run() {}
}

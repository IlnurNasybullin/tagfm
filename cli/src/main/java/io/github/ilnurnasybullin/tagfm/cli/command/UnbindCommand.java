package io.github.ilnurnasybullin.tagfm.cli.command;

import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "unbind", subcommands = {
        UnbindFilesCommand.class
})
public class UnbindCommand implements Runnable {
    @Override
    public void run() {

    }
}

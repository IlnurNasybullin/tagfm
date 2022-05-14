package io.github.ilnurnasybullin.tagfm.cli.command.print;

import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "print", subcommands = {
        PrintGeneralStateCommand.class,
        PrintTagTreeCommand.class
})
public class PrintCommand implements Runnable {
    @Override
    public void run() {

    }
}

package io.github.ilnurnasybullin.tagfm.cli.command;

import jakarta.inject.Singleton;
import picocli.CommandLine;

@CommandLine.Command(name = "namespace", subcommands = {
        NamespaceRenameCommand.class,
        NamespaceChangeCommand.class
})
@Singleton
public class NamespaceCommand implements Runnable {
    @Override
    public void run() {}
}

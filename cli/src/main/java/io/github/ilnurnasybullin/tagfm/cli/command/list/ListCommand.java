package io.github.ilnurnasybullin.tagfm.cli.command.list;

import jakarta.inject.Singleton;
import picocli.CommandLine;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "list", subcommands = {
        ListFilesCommand.class,
        ListFileTagsCommand.class,
        ListTagsCommand.class
})
public class ListCommand implements Runnable {
    @Override
    public void run() {

    }
}

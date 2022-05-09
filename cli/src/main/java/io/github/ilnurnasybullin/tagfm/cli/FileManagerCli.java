package io.github.ilnurnasybullin.tagfm.cli;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.micronaut.context.ApplicationContext;
import picocli.CommandLine;

public class FileManagerCli {

    public static void main(String[] args) {
        try(ApplicationContext context = ApplicationContext.run(args);
            FileManagerCommand command = context.getBean(FileManagerCommand.class)) {
            CommandLine commandLine = new CommandLine(command, context::getBean);
            commandLine.setCaseInsensitiveEnumValuesAllowed(true)
                    .execute(args);
        }
    }

}

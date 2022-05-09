package io.github.ilnurnasybullin.tagfm.cli;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.micronaut.context.ApplicationContext;
import picocli.CommandLine;

public class FileManagerCli {

    public static void main(String[] args) {
        try(ApplicationContext context = ApplicationContext.run(args);
            FileManagerCommand command = context.getBean(FileManagerCommand.class)) {
            new CommandLine(command, context::getBean)
                    .setCaseInsensitiveEnumValuesAllowed(true)
                    .execute(args);
        }
    }

}

package io.github.ilnurnasybullin.tagfm.cli;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.CustomFactory;
import io.micronaut.context.ApplicationContext;
import picocli.CommandLine;

public class FileManagerCli {

    public static void main(String[] args) {

        String[][] strings = {
//                {"init", "test"}
        };

        try(ApplicationContext context = ApplicationContext.run(args);
            FileManagerCommand command = context.getBean(FileManagerCommand.class)) {
            CommandLine.IFactory cfFactory = CommandLine.defaultFactory();
            CustomFactory factory = new CustomFactory(context, cfFactory);
            CommandLine commandLine = new CommandLine(command, factory)
                    .setCaseInsensitiveEnumValuesAllowed(true);

            for (String[] customArgs: strings) {
                commandLine.execute(customArgs);
            }
        }
    }

}

package io.github.ilnurnasybullin.tagfm.cli;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.CustomFactory;
import io.micronaut.context.ApplicationContext;
import picocli.CommandLine;

public class FileManagerCli {

    public static void main(String[] args) {

        String[][] strings = {
//                {"init", "test"},
//                {"add-tags", "OS/windows,OS/linux/gnome,OS/MacOS,OS/linux/Android,OS/linux/kubuntu,OS/linux/ubuntu"},
//                {"bind", "files", "-t", "OS/linux", ".idea"},
//                {"bind", "files", "-t", "OS/linux/gnome", ".gradle"},
//                {"bind", "synonyms", "OS/linux/kubuntu,OS/linux/ubuntu"},
//                {"unbind", "file-tags", ".gradle", "-t", "OS/windows", "-frp", "remove_if_no_tags"},
//                {"remove-tag", "OS/linux", "-trs", "up_and_merge_children"},
//                {"bind", "parent", "OS/Android", "OS/kubuntu"},
//                {"add-tags", "OS/linux/gnome"},
//                {"print", "general-state"},
                {"print", "tag-tree", "-sn", "OS"}
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

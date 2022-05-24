package io.github.ilnurnasybullin.tagfm.cli.command.option;

import jakarta.inject.Singleton;
import picocli.CommandLine;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        synopsisHeading = "Usage:%n\t",
        descriptionHeading = "%nDescription:%n%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n",
        commandListHeading = "%nCommands:%n"
)
public class ReusableOption {

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "helper")
    protected boolean helpRequest;

}

package io.github.ilnurnasybullin.tagfm.cli.command.mixin;

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
        optionListHeading = "%nOptions:%n",
        commandListHeading = "%nCommands:%n"
)
public class HelpOption {

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "helper")
    protected boolean helpRequest;

}

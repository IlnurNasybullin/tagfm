package io.github.ilnurnasybullin.tagfm.cli.command.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(
        name = "switch",
        headerHeading = "Usage:%n%n",
        header = "Working namespace switching",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "switch working namespace"
)
public class NamespaceSwitch implements Runnable {

    private final NamespaceRepositoryService<NamespaceView> namespaceService;

    @CommandLine.Parameters(index = "0", arity = "1", description = "switching namespace")
    private String workingNamespaceName;

    @CommandLine.Mixin
    private HelpOption helper;

    public NamespaceSwitch(NamespaceRepositoryService<NamespaceView> namespaceService) {
        this.namespaceService = namespaceService;
    }

    @Override
    public void run() {
        namespaceService.setWorkingNamespace(workingNamespaceName);
    }
}

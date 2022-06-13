package io.github.ilnurnasybullin.tagfm.cli.command.print.list;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Objects;

@Singleton
@CommandLine.Command(
        name = "namespaces",
        headerHeading = "Usage:%n%n",
        header = "Namespaces printing",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "printing list of namespaces"
)
public class ListNamespaces implements Runnable {

    private final NamespaceRepositoryService<NamespaceView> namespaceService;

    @CommandLine.Mixin
    private HelpOption helper;

    public ListNamespaces(NamespaceRepositoryService<NamespaceView> namespaceService) {
        this.namespaceService = namespaceService;
    }

    @Override
    public void run() {
        String workingNamespaceName = namespaceService.getWorkingNamespace().map(NamespaceView::name).orElse(null);
        String prefix = "\t";
        String curPrefix = ">>  ";

        namespaceService.getAll()
                .stream()
                .map(NamespaceView::name)
                .map(name -> String.format("%s%s", Objects.equals(name, workingNamespaceName) ? curPrefix : prefix, name))
                .forEach(System.out::println);
    }
}

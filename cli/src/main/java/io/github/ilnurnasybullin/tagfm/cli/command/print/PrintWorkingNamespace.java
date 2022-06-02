package io.github.ilnurnasybullin.tagfm.cli.command.print;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "working-namespace")
public class PrintWorkingNamespace implements Runnable {

    private final NamespaceRepositoryService<NamespaceView> namespaceService;

    public PrintWorkingNamespace(NamespaceRepositoryService<NamespaceView> namespaceService) {
        this.namespaceService = namespaceService;
    }


    @Override
    public void run() {
        String info = namespaceService.getWorkingNamespace()
                .map(NamespaceView::name)
                .orElse("No working namespace!");
        System.out.println(info);
    }
}

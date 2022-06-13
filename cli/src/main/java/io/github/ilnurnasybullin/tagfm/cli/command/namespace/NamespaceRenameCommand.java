/*
 * Copyright 2022 Ilnur Nasybullin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ilnurnasybullin.tagfm.cli.command.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@CommandLine.Command(
        name = "rename",
        headerHeading = "Usage:%n%n",
        header = "Namespace renaming",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "namespace renaming"
)
@Singleton
public class NamespaceRenameCommand implements Runnable {

    private final NamespaceRepositoryService<NamespaceView> namespaceService;

    @CommandLine.Parameters(index = "0", arity = "1", description = "old namespace name")
    private String oldName;

    @CommandLine.Parameters(index = "1", arity = "1", description = "new namespace name")
    private String newName;

    @CommandLine.Mixin
    private HelpOption helper;

    public NamespaceRenameCommand(NamespaceRepositoryService<NamespaceView> namespaceService) {
        this.namespaceService = namespaceService;
    }

    @Override
    public void run() {
        NamespaceView namespace = namespaceService.find(oldName).orElseThrow(() ->
                new NamespaceNotFoundException(String.format("Namespace with name [%s] isn't found!", oldName))
        );
        namespaceService.replace(newName, namespace);
    }
}

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

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@CommandLine.Command(name = "edit")
@Singleton
public class NamespaceEditCommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1")
    private String namespaceName;

    @CommandLine.Option(names = {"-fns", "--file-naming-strategy"})
    private FileNamingStrategy strategy;

    private final NamespaceRepositoryService<NamespaceView> namespaceService;

    public NamespaceEditCommand(NamespaceRepositoryService<NamespaceView> namespaceService) {
        this.namespaceService = namespaceService;
    }

    @Override
    public void run() {
        if (strategy != null) {
            NamespaceView namespace = namespaceService.find(namespaceName)
                    .orElseThrow(() -> new NamespaceNotFoundException(
                            String.format("Namespace with name [%s] isn't found!", namespaceName)
                    ));
            if (namespace.fileNaming() != strategy) {
                namespace.replaceFileNamingStrategy(strategy);
                namespaceService.commit(namespace);
            }
        }
    }
}

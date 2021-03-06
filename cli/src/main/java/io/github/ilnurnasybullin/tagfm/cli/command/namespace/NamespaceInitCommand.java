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
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@CommandLine.Command(
        name = "init",
        headerHeading = "Usage:%n%n",
        header = "Namespace initialization",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "init namespace in '.tagfm' folder (it's creating automatically"
)
@Singleton
public class NamespaceInitCommand implements Runnable {

    private final NamespaceRepositoryService<NamespaceView> namespaceService;

    @CommandLine.Option(
            names = {"-fns", "--file-naming-strategy"},
            paramLabel = "file naming strategy",
            description = "file naming strategy, default is ${DEFAULT-VALUE}. Valid strategies: ${COMPLETION-CANDIDATES}"
    )
    private FileNamingStrategy fileNaming = FileNamingStrategy.RELATIVE;

    @CommandLine.Parameters(description = "namespace's name")
    private String name;

    @CommandLine.Mixin
    private HelpOption helper;

    public NamespaceInitCommand(NamespaceRepositoryService<NamespaceView> namespaceService) {
        this.namespaceService = namespaceService;
    }

    @Override
    public void run() {
        NamespaceView namespace = namespaceService.init(name, fileNaming);
        namespaceService.commit(namespace);
        namespaceService.setWorkingNamespace(name);
    }
}

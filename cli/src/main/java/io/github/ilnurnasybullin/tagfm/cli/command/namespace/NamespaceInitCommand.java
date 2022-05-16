/*
 * Copyright 2022 Ilnur Nasybullin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.ilnurnasybullin.tagfm.cli.command.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceServiceImpl;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Optional;

@CommandLine.Command(name = "init")
@Singleton
public class NamespaceInitCommand implements Runnable {

    private final FileManagerCommand fileManager;
    private final NamespaceServiceImpl namespaceService;

    @CommandLine.Option(names = {"-fns", "--file-naming-strategy"})
    private FileNamingStrategy fileNaming = FileNamingStrategy.RELATIVE;

    @CommandLine.Parameters
    private String name;

    public NamespaceInitCommand(FileManagerCommand fileManager, NamespaceServiceImpl namespaceService) {
        this.fileManager = fileManager;
        this.namespaceService = namespaceService;
    }

    @Override
    public void run() {
        fileManager.checkNamespaceOnNonExisting();
        fileManager.setWriteMode();
        fileManager.initNamespace(Optional.of(namespaceService.init(name, fileNaming)));
    }
}

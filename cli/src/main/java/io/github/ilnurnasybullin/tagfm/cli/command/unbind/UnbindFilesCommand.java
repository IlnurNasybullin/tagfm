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

package io.github.ilnurnasybullin.tagfm.cli.command.unbind;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Singleton
@CommandLine.Command(name = "files")
public class UnbindFilesCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1")
    private List<Path> files;

    public UnbindFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        List<TaggedFileDto> files = getFiles(namespace);
        Set<TaggedFileDto> namespaceFiles = namespace.files();
        files.forEach(namespaceFiles::remove);
        fileManager.setWriteMode();
    }

    private List<TaggedFileDto> getFiles(NamespaceDto namespace) {
        return new NamespaceFileManagerFacade()
                .find(files, namespace)
                .toList();
    }
}

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

package io.github.ilnurnasybullin.tagfm.cli.command.file.unbind;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileManager;
import io.github.ilnurnasybullin.tagfm.api.service.TaggedFileNotFoundException;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Singleton
@CommandLine.Command(
        name = "remove",
        headerHeading = "Usage:%n%n",
        header = "Remove files from namespace",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "remove files from namespace"
)
public class RemoveFilesCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0..*", description = "files for removing from namespace")
    private List<Path> files;

    @CommandLine.Mixin
    private HelpOption helper;

    public RemoveFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        List<TaggedFileView> files = getFiles(namespace);
        Set<TaggedFileView> namespaceFiles = namespace.files();
        files.forEach(namespaceFiles::remove);
        fileManager.commit();
    }

    private List<TaggedFileView> getFiles(NamespaceView namespace) {
        return FileManager.of(namespace)
                .findExact(files)
                .toList();
    }

    private TaggedFileNotFoundException fileNotExist(String fileName, String namespaceName) {
        return new TaggedFileNotFoundException(
                String.format("File [%s] isn't existing in namespace [%s]", fileName, namespaceName)
        );
    }
}

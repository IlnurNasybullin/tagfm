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

package io.github.ilnurnasybullin.tagfm.cli.command.file;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileTagsReplacer;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "replace",
        headerHeading = "Usage:%n%n",
        header = "Replacing file in namespace with moving tags",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "replacing file in namespace with moving tags."
)
public class FileReplaceCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0", description = "file being replaced from namespace")
    private Path src;

    @CommandLine.Parameters(arity = "1", index = "1", description = "replacing file from outside namespace")
    private Path dest;

    @CommandLine.Mixin
    private HelpOption helper;

    public FileReplaceCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();

        FileManager fileFinder = FileManager.of(namespace);
        TaggedFileView file = fileFinder.findExact(src);
        FileTagsReplacer.of(namespace).replace(file, dest);
        fileManager.commit();
    }
}

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

package io.github.ilnurnasybullin.tagfm.cli.command.copyTags;

import io.github.ilnurnasybullin.tagfm.cli.command.CopyTagsStrategy;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "copy-tags")
public class CopyTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0")
    private Path src;

    @CommandLine.Parameters(arity = "1", index = "1")
    private Path dest;

    @CommandLine.Option(names = {"-c", "--create-dest"}, arity = "0")
    private boolean createIfNotExist;

    @CommandLine.Option(names = {"-cts", "--copy-tags-strategy"})
    private CopyTagsStrategy copyTagsStrategy = CopyTagsStrategy.ADD;

    public CopyTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();

        NamespaceFileManagerFacade facade = new NamespaceFileManagerFacade();
        TaggedFileDto srcTaggedFile = facade.findExact(src, namespace);

        TaggedFileDto destTaggedFile = createIfNotExist ?
                facade.findOrCreate(dest, namespace) :
                facade.findExact(dest, namespace);

        namespace.files().add(destTaggedFile);

        if (copyTagsStrategy == CopyTagsStrategy.REPLACE) {
            destTaggedFile.tags().clear();
        }

        destTaggedFile.tags().addAll(srcTaggedFile.tags());

        fileManager.setWriteMode();
    }
}

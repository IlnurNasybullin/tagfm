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

package io.github.ilnurnasybullin.tagfm.cli.command.file.merge;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.option.ReusableOption;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "copy-tags",
        headerHeading = "Usage:%n%n",
        header = "Copying tags from one file to other",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = """
                Copying tags from one tagged file (src) to other (dest). Dest file can be added to namespace, if needed. \
                If dest file already exist, tags from src file can be added or replaced.
                """
)
public class CopyCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0", description = "src file of tags")
    private Path src;

    @CommandLine.Parameters(arity = "1", index = "1", description = "dest file")
    private Path dest;

    @CommandLine.Option(names = {"-c", "--create-dest"}, arity = "0",
            description = "create dest file, if it isn't existing in namespace"
    )
    private boolean createIfNotExist;

    @CommandLine.Option(names = {"-cts", "--copy-strategy"},
            description = "strategy of copying tags, default is ${DEFAULT-VALUE}. Valid strategies: ${COMPLETION-CANDIDATES}"
    )
    private CopyStrategy copyStrategy = CopyStrategy.ADD;

    @CommandLine.Mixin
    private ReusableOption options;

    public CopyCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();

        NamespaceFileManagerFacade facade = new NamespaceFileManagerFacade();
        TaggedFile srcTaggedFile = facade.findExact(src, namespace);

        TaggedFile destTaggedFile = createIfNotExist ?
                facade.findOrCreate(dest, namespace) :
                facade.findExact(dest, namespace);

        namespace.files().add(destTaggedFile);

        if (copyStrategy == CopyStrategy.REPLACE) {
            destTaggedFile.tags().clear();
        }

        destTaggedFile.tags().addAll(srcTaggedFile.tags());

        fileManager.setWriteMode();
    }
}

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
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.api.service.NamespaceTagAdder;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Singleton
@CommandLine.Command(name = "bind")
public class TagBindsCommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1")
    private Path file;

    @CommandLine.Parameters(index = "1..*", arity = "1")
    private final List<String> tags = new ArrayList<>();

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-c", "--create"})
    private boolean createTag;

    private final FileManagerCommand fileManager;

    public TagBindsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();
        List<Tag> tags = getTags(namespace);

        TaggedFile taggedFile = getFile(namespace);
        taggedFile.tags().addAll(tags);
        namespace.files().add(taggedFile);
        fileManager.setWriteMode();
    }

    private TaggedFile getFile(Namespace namespace) {
        NamespaceFileManagerFacade facade = new NamespaceFileManagerFacade();
        return facade.findOrCreate(file, namespace);
    }

    private List<Tag> getTags(Namespace namespace) {
        NamespaceTagSearcherFacade tagSearcher = new NamespaceTagSearcherFacade();
        if (!createTag) {
            return tagSearcher.searchTags(tags, namespace, shortName)
                    .toList();
        }

        List<Tag> tags = tagSearcher.searchOrCreate(this.tags, namespace, shortName).toList();
        NamespaceTagAdder.of(namespace).addTags(tags);
        return tags;
    }
}

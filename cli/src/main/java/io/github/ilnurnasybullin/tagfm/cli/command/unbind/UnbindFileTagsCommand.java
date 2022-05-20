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

package io.github.ilnurnasybullin.tagfm.cli.command.unbind;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceFileManagerFacade;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Singleton
@CommandLine.Command(name = "file-tags")
public class UnbindFileTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1")
    private final List<Path> files = new ArrayList<>();

    @CommandLine.Option(names = {"-t", "--tags"})
    private final List<String> tags = new ArrayList<>();

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-frp", "--file-removing-policy"}, paramLabel = "file removing policy")
    private FileRemovingPolicy removingPolicy = FileRemovingPolicy.NO_REMOVE;

    public UnbindFileTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();
        Collection<Tag> tags = getTags(namespace);
        getFiles(namespace).forEach(file -> tags.forEach(file.tags()::remove));

        if (removingPolicy == FileRemovingPolicy.REMOVE_IF_NO_TAGS) {
            namespace.files().removeIf(file -> file.tags().isEmpty());
        }

        fileManager.setWriteMode();
    }

    private List<Tag> getTags(Namespace namespace) {
        return tags.isEmpty() ?
                List.of() : new NamespaceTagSearcherFacade()
                .searchTags(tags, namespace, shortName)
                .toList();
    }

    private Stream<TaggedFile> getFiles(Namespace namespace) {
        return new NamespaceFileManagerFacade()
                .find(files, namespace);
    }
}

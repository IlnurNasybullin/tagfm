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
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.NamespaceTagAdding;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
@CommandLine.Command(name = "bind")
public class FileTagBindsCommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1")
    private Path file;

    @CommandLine.Parameters(index = "1..*", arity = "1")
    private final List<String> tags = new ArrayList<>();

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-c", "--create"})
    private boolean createTag;

    private final FileManagerCommand fileManager;

    public FileTagBindsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        Collection<TagView> tags = getTags(namespace);

        TaggedFileView taggedFile = FileManager.of(namespace).findOrCreate(file);
        taggedFile.tags().addAll(tags);
        namespace.files().add(taggedFile);
        fileManager.commit();
    }

    private Collection<TagView> getTags(NamespaceView namespace) {
        TagService tagService = TagService.of(namespace);
        if (!createTag) {
            return shortName ?
                    tagService.findByNamesExact(tags).values() :
                    tagService.findByFullNamesExact(tags).values();
        }

        Collection<TagView> tagViews = shortName ?
                tagService.findOrCreateByNamesExact(tags).values() :
                tagService.findOrCreateByFullNames(tags).values();

        NamespaceTagAdding tagAdder = NamespaceTagAdding.of(namespace);
        tagAdder.addTags(tagViews);
        return tagViews;
    }
}
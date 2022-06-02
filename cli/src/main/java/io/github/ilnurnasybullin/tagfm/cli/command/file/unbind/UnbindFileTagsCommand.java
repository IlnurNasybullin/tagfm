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
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
@CommandLine.Command(name = "unbind")
public class UnbindFileTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0")
    private Path file;

    @CommandLine.Parameters(arity = "1", index = "1..*")
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
        NamespaceView namespace = fileManager.namespaceOrThrow();
        Collection<TagView> tags = getTags(namespace);
        TaggedFileView taggedFile = FileManager.of(namespace).findExact(file);
        taggedFile.tags().removeAll(tags);

        if (removingPolicy == FileRemovingPolicy.REMOVE_IF_NO_TAGS && taggedFile.tags().isEmpty()) {
            namespace.files().remove(taggedFile);
        }

        fileManager.commit();
    }

    private Collection<TagView> getTags(NamespaceView namespace) {
        if (tags.isEmpty()) {
            return List.of();
        }

        TagService tagFinder = TagService.of(namespace);
        return shortName ?
                tagFinder.findByNamesExact(tags).values() :
                tagFinder.findByFullNamesExact(tags).values();
    }
}

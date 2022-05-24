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

package io.github.ilnurnasybullin.tagfm.cli.command.tag;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.service.NamespaceTagRemover;
import io.github.ilnurnasybullin.tagfm.api.service.TagRemovingStrategy;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "remove")
public class TagRemoveCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-trs", "--tag-removing-strategy"})
    private TagRemovingStrategy tagRemovingStrategy = TagRemovingStrategy.UP_CHILDREN_WITHOUT_CONFLICTS;

    @CommandLine.Parameters(index = "0", arity = "1")
    private String tagName;

    public TagRemoveCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();
        Tag searchedTag = searchTag(tagName, namespace);
        NamespaceTagRemover.of(namespace).removeTag(searchedTag, tagRemovingStrategy);
        fileManager.setWriteMode();
    }

    private Tag searchTag(String name, Namespace namespace) {
        return new NamespaceTagSearcherFacade().searchTag(name, namespace, shortName);
    }
}

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

package io.github.ilnurnasybullin.tagfm.cli.command.bind;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.TagParentBindingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TagParentBinding;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(name = "parent")
public class BindParentCommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1")
    private String parentTag;

    @CommandLine.Parameters(index = "1", arity = "1")
    private String childTag;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-pbs", "--parent-binding-strategy"})
    private TagParentBindingStrategy parentBindingStrategy = TagParentBindingStrategy.THROW_IF_COLLISION;

    private final FileManagerCommand fileManager;

    public BindParentCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        TreeTagDto parentTag = getTag(namespace, this.parentTag);
        TreeTagDto childTag = getTag(namespace, this.childTag);

        TagParentBinding.of(namespace).bindParent(childTag, parentTag, parentBindingStrategy);
        fileManager.setWriteMode();
    }

    private TreeTagDto getTag(NamespaceDto namespace, String tagName) {
        NamespaceTagSearcherFacade facade = new NamespaceTagSearcherFacade();
        return facade.searchTag(tagName, namespace, shortName);
    }
}

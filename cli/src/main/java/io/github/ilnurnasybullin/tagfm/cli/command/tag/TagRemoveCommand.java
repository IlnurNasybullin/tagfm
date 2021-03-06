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

import io.github.ilnurnasybullin.tagfm.api.service.TagRemovingStrategy;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.NamespaceTagRemover;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(
        name = "remove",
        headerHeading = "Usage:%n%n",
        header = "Removing tags from namespace",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = """
                removing tags from current namespace. Formally, removing tag X from namespace is equivalent to
                unbind tag X from his parent (tag Y) without binding tag X to any tag. Tag X can have any child, so for
                removing has 5 strategies: \
                    * up children without conflicts - all child tags of tag X bind to tag Y; if tag has already tags \
                with same names (collision tags) would be thrown exception;
                    * remove children - remove tag X and his child tags;
                    * up and use old - remove tag X and collision tags (child tags of tag X);
                    * up and use new - remove tag X and collision tags (child tags of tag Y);
                    * up and merge - remove tag X and merge collision tags (union files and synonym groups).
                """
)
public class TagRemoveCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"}, description = "search tag by short name")
    private boolean shortName;

    @CommandLine.Option(
            names = {"-trs", "--tag-removing-strategy"},
            description = "tag removing strategy, default is ${DEFAULT-VALUE}. Valid strategies: ${COMPLETION-CANDIDATES}"
    )
    private TagRemovingStrategy tagRemovingStrategy = TagRemovingStrategy.UP_CHILDREN_WITHOUT_CONFLICTS;

    @CommandLine.Parameters(index = "0", arity = "1", description = "removing tag")
    private String tagName;

    public TagRemoveCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TagView searchedTag = searchTag(namespace);
        NamespaceTagRemover.of(namespace).removeTag(searchedTag, tagRemovingStrategy);
        fileManager.commit();
    }

    private TagView searchTag(NamespaceView namespace) {
        TagService tagService = TagService.of(namespace);
        return shortName ?
                tagService.findByNameExact(tagName) :
                tagService.findByFullNameExact(tagName);
    }
}

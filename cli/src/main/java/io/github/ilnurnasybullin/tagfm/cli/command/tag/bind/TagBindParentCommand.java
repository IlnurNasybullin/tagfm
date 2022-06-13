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

package io.github.ilnurnasybullin.tagfm.cli.command.tag.bind;

import io.github.ilnurnasybullin.tagfm.api.service.TagParentBindingStrategy;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagParentBinder;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(
        name = "parent",
        headerHeading = "Usage:%n%n",
        header = "Tags' hierarchy binding",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = """
        binding tags with hierarchy relationship. Formally, binding (hierarchy) tag X to tag Y  is equivalent that tag X \
        is setted child of tag Y. For binding can be used these binding strategies: \
            * throw if collision - throw exception, if tag Y has already tag with same short name (collision tag); \
            * use old - remove new tags (X and his child) for collision tags; \
            * use new - remove old tags (child tags of Y) for collision tags; \
            * merge - union files and synonyms of collision tags.
        """
)
public class TagBindParentCommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1", description = "parent tag")
    private String parentTag;

    @CommandLine.Parameters(index = "1", arity = "1", description = "child tag")
    private String childTag;

    @CommandLine.Option(names = {"-sn", "--short-name"}, description = "search tags by short name")
    private boolean shortName;

    @CommandLine.Option(
            names = {"-pbs", "--parent-binding-strategy"},
            description = "tag parent binding strategy, default is ${DEFAULT-VALUE}. Valid strategies: ${COMPLETION-CANDIDATES}"
    )
    private TagParentBindingStrategy parentBindingStrategy = TagParentBindingStrategy.THROW_IF_COLLISION;

    @CommandLine.Mixin
    private HelpOption helper;

    private final FileManagerCommand fileManager;

    public TagBindParentCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TagView parentTag = getTag(namespace, this.parentTag);
        TagView childTag = getTag(namespace, this.childTag);

        TagParentBinder.of(namespace).bind(childTag, parentTag, parentBindingStrategy);
        fileManager.commit();
    }

    private TagView getTag(NamespaceView namespace, String tagName) {
        TagService tagService = TagService.of(namespace);
        return shortName ?
                tagService.findByNameExact(tagName) :
                tagService.findByFullNameExact(tagName);
    }
}

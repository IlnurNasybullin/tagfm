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

package io.github.ilnurnasybullin.tagfm.cli.command.tag.unbind;

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
        name = "parent-tag",
        headerHeading = "Usage:%n%n",
        header = "Hierarchy's unbinding of tag",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = """
                unbind tags' hierarchy relationships. Formally, unbinding tag X from parent tag Y is defining that tag X \
                would be child of namespace system root tag (binding tag X with namespace system rooo tag). For binding info \
                enter command help: tagfm tag bind --help
                """
)
public class UnbindParentTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"}, description = "search unbinding tag by short name")
    private boolean shortName;

    @CommandLine.Option(
            names = {"-pbs", "--parent-binding-strategy"},
            description = "tag parent binding strategy, default is ${DEFAULT-VALUE}. Valid strategies: ${COMPLETION-CANDIDATES}"
    )
    private TagParentBindingStrategy parentBindingStrategy = TagParentBindingStrategy.THROW_IF_COLLISION;

    @CommandLine.Parameters(index = "0", arity = "1", description = "unbinding tag")
    private String unbindingTag;

    @CommandLine.Mixin
    private HelpOption helper;

    public UnbindParentTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TagService tagService = TagService.of(namespace);
        TagView tag = shortName ?
                tagService.findByNameExact(unbindingTag) :
                tagService.findByFullNameExact(unbindingTag);
        TagParentBinder.of(namespace).unbind(tag, parentBindingStrategy);
        fileManager.commit();
    }
}

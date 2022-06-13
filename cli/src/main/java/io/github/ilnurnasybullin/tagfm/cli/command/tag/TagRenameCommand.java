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
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine;

@Singleton
@CommandLine.Command(
        name = "rename",
        headerHeading = "Usage:%n%n",
        header = "Renaming tag in working namespace",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "rename tag int working namespace"
)
public class TagRenameCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"}, description = "search tag by short name")
    private boolean shortName = false;

    @CommandLine.Parameters(index = "0", arity = "1", description = "old tag name")
    private String oldName;

    @CommandLine.Parameters(index = "1", arity = "1", description = "new tag name")
    private String newName;

    @CommandLine.Mixin
    private HelpOption helper;

    public TagRenameCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TagView searchedTag = searchTag(oldName, namespace);
        searchedTag.rename(newName);
        fileManager.commit();
    }

    private TagView searchTag(String tagName, NamespaceView namespace) {
        TagService tagService = TagService.of(namespace);
        return shortName ?
                tagService.findByNameExact(tagName) :
                tagService.findByFullNameExact(tagName);
    }
}

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

package io.github.ilnurnasybullin.tagfm.cli.command.print.list;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "synonyms",
        headerHeading = "Usage:%n%n",
        header = "Synonym tags' printing",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "printing list of synonym tags"
)
public class ListSynonymsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0", paramLabel = "tag", description = "tag for searching synonyms")
    private String tagName;

    @CommandLine.Option(names = {"-sn", "--short-name"}, description = "searching tag by short name")
    private boolean shortName;

    @CommandLine.Mixin
    private HelpOption helper;

    public ListSynonymsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TagView tag = getTag(namespace);
        namespace.synonymsManager()
                .synonyms(tag)
                .stream()
                .filter(t -> t != tag)
                .map(TagView::fullName)
                .forEach(System.out::println);
    }

    private TagView getTag(NamespaceView namespace) {
        TagService tagService = TagService.of(namespace);
        return shortName ?
                tagService.findByNameExact(tagName) :
                tagService.findByFullNameExact(tagName);
    }
}

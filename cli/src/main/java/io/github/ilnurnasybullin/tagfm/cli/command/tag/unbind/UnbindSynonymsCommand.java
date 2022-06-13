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

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.SynonymTagManagerView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
@CommandLine.Command(
        name = "synonyms",
        headerHeading = "Usage:%n%n",
        header = "Synonym's unbinding of tags",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "synonym's relationship unbinding"
)
public class UnbindSynonymsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1", index = "0", description = "unbinding tags from synonym relationship")
    private final List<String> tags = new ArrayList<>();

    @CommandLine.Option(names = {"-sn", "--short-name"}, description = "search tags by short name")
    private boolean shortName;

    @CommandLine.Mixin
    private HelpOption helper;

    public UnbindSynonymsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        Collection<TagView> tags = getTags(namespace);
        SynonymTagManagerView synonymsManager = namespace.synonymsManager();
        tags.forEach(synonymsManager::unbind);
        fileManager.commit();
    }

    private Collection<TagView> getTags(NamespaceView namespace) {
        TagService tagService = TagService.of(namespace);
        return shortName ?
                tagService.findByNamesExact(tags).values() :
                tagService.findByFullNamesExact(tags).values();
    }
}
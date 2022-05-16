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

package io.github.ilnurnasybullin.tagfm.cli.command.unbind;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;

@Singleton
@CommandLine.Command(name = "synonyms")
public class UnbindSynonymsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(arity = "1")
    private final List<String> tags = new ArrayList<>();

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    public UnbindSynonymsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceDto namespace = fileManager.namespaceOrThrow();
        List<TreeTagDto> tags = getTags(namespace);
        SynonymTagManager synonymsManager = namespace.synonymsManager();
        tags.forEach(synonymsManager::unbind);
        fileManager.setWriteMode();
    }

    private List<TreeTagDto> getTags(NamespaceDto namespace) {
        return new NamespaceTagSearcherFacade()
                .searchTags(tags, namespace, shortName)
                .toList();
    }
}
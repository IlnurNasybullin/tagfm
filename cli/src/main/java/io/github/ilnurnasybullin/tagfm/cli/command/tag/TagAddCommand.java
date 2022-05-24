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
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.service.NamespaceTagAdder;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagCreator;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@CommandLine.Command(name = "add", description = "add tags in current namespace")
public class TagAddCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(index = "*", paramLabel = "tags", arity = "1..*",
            description = "adding tags in current namespace"
    )
    private final List<String> tags = new ArrayList<>();

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "help command")
    private boolean helpRequest;

    public TagAddCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();

        TagCreator creator = new TagCreator();
        List<Tag> treeTags = tags.stream()
                .map(creator::deepCreate)
                .flatMap(Optional::stream)
                .toList();

        NamespaceTagAdder.of(namespace).addTags(treeTags);
        fileManager.setWriteMode();
    }
}
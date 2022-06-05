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

package io.github.ilnurnasybullin.tagfm.cli.command;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRepositoryService;
import io.github.ilnurnasybullin.tagfm.cli.command.file.FileCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.namespace.NamespaceCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.namespace.NamespaceNotFoundException;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.ReusableOption;
import io.github.ilnurnasybullin.tagfm.cli.command.print.PrintCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.tag.TagCommand;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.io.Closeable;
import java.util.Optional;

@CommandLine.Command(
        name = "tagfm",
        version = FileManagerCommand.version,
        subcommands = {
            FileCommand.class,
            NamespaceCommand.class,
            PrintCommand.class,
            TagCommand.class
        },
        description = """
                TagFM is a file manager for tagging files and folders, managing tags and searching files by logical \
                tags query.
                """
)
@Singleton
public class FileManagerCommand implements Runnable, Closeable {

    final static String version = "0.0.1";

    private Optional<NamespaceView> namespace;
    private final NamespaceRepositoryService<NamespaceView> namespaceService;

    private boolean onCommit = false;

    @CommandLine.Mixin
    private ReusableOption options;

    public FileManagerCommand(NamespaceRepositoryService<NamespaceView> namespaceService) {
        this.namespaceService = namespaceService;
        this.namespace = Optional.empty();
    }

    public void initNamespace(Optional<NamespaceView> namespace) {
        this.namespace = namespace;
    }

    public NamespaceView namespaceOrThrow() {
        initNamespace(namespaceService.getWorkingNamespace());
        return namespace.orElseThrow(() -> new NamespaceNotFoundException("Working namespace isn't founded!"));
    }

    public void commit() {
        onCommit = true;
    }

    @Override
    public void close() {
        if (onCommit) {
            namespace.ifPresent(namespaceService::commit);
        }
    }

    @Override
    public void run() {}
}

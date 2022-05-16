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

import io.github.ilnurnasybullin.tagfm.cli.command.addFiles.AddTagsCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.bind.BindCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.copyTags.CopyTagsCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.list.ListCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.namespace.NamespaceCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.namespace.NamespaceInitCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.namespace.NamespaceNotInitializedException;
import io.github.ilnurnasybullin.tagfm.cli.command.print.PrintCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.removeTag.RemoveTagCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.renameTag.RenameTagCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.replaceFile.ReplaceFileCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.searchFiles.SearchFilesCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.unbind.UnbindCommand;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceAlreadyInitialized;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.io.Closeable;
import java.util.Optional;

@CommandLine.Command(name = "tagfm", subcommands = {
        NamespaceInitCommand.class,
        NamespaceCommand.class,
        AddTagsCommand.class,
        RenameTagCommand.class,
        RemoveTagCommand.class,
        BindCommand.class,
        UnbindCommand.class,
        PrintCommand.class,
        ListCommand.class,
        CopyTagsCommand.class,
        ReplaceFileCommand.class,
        SearchFilesCommand.class
})
@Singleton
public class FileManagerCommand implements Runnable, Closeable {

    private Optional<NamespaceDto> namespace;
    private final NamespaceServiceImpl namespaceService;

    private boolean onCommit = false;

    public FileManagerCommand(NamespaceServiceImpl namespaceService) {
        this.namespaceService = namespaceService;
    }

    @PostConstruct
    private void initNamespace() {
        initNamespace(namespaceService.find());
    }

    public void initNamespace(Optional<NamespaceDto> namespace) {
        this.namespace = namespace;
    }

    public void checkNamespaceOnNonExisting() {
        namespace.ifPresent(namespace -> {
            throw new NamespaceAlreadyInitialized(
                    String.format("Namespace [%s] has already initialized!", namespace.name())
            );
        });
    }

    public NamespaceDto namespaceOrThrow() {
        return namespace.orElseThrow(() -> new NamespaceNotInitializedException("Namespace isn't initialized!"));
    }

    public void setWriteMode() {
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

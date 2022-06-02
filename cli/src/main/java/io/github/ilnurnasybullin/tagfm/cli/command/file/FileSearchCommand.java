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

package io.github.ilnurnasybullin.tagfm.cli.command.file;

import io.github.ilnurnasybullin.tagfm.api.service.FileSearchStrategy;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.format.TableFormatPrinter;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.TaggedFileSearcher;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "search")
public class FileSearchCommand implements Runnable {

    @CommandLine.Parameters(arity = "1", index = "0..*")
    private final List<String> tokens = new ArrayList<>();

    @CommandLine.Option(names = {"-fss", "--file-search-strategy"})
    private FileSearchStrategy fileSearchStrategy = FileSearchStrategy.SIMPLE;

    private final FileManagerCommand fileManager;

    public FileSearchCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TaggedFileSearcher factory = new TaggedFileSearcher(namespace);

        Set<TaggedFileView> files = factory.searchFiles(tokens, fileSearchStrategy);
        printTags(files);
    }

    private void printTags(Set<TaggedFileView> files) {
        if (files.isEmpty()) {
            System.out.println("Files are not found!");
            return;
        }

        TableFormatPrinter printer = new TableFormatPrinter("%50s | %50s%n", 103);
        printer.print(new String[]{"name", "tags"}, files.stream(),
                List.of(TaggedFileView::file,
                        file -> file
                                .tags()
                                .stream().map(TagView::fullName)
                                .collect(Collectors.joining(", "))
                )
        );

        System.out.println();
    }
}

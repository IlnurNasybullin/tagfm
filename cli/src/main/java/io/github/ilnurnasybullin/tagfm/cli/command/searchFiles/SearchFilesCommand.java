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

package io.github.ilnurnasybullin.tagfm.cli.command.searchFiles;

import io.github.ilnurnasybullin.tagfm.api.service.FileSearchStrategy;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.format.TableFormatPrinter;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
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
@CommandLine.Command(name = "search-files")
public class SearchFilesCommand implements Runnable {

    @CommandLine.Parameters(arity = "1..", index = "0")
    private final List<String> tokens = new ArrayList<>();

    @CommandLine.Option(names = {"-fss", "--file-search-strategy"})
    private FileSearchStrategy fileSearchStrategy = FileSearchStrategy.SIMPLE;

    private final FileManagerCommand fileManager;

    public SearchFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();
        TaggedFileSearcher factory = new TaggedFileSearcher(namespace);

        Set<TaggedFile> files = factory.searchFiles(tokens, fileSearchStrategy);
        printTags(files);
    }

    private void printTags(Set<TaggedFile> files) {
        if (files.isEmpty()) {
            System.out.println("Files are not found!");
            return;
        }

        TableFormatPrinter printer = new TableFormatPrinter("%50s | %50s%n", 103);
        printer.print(new String[]{"name", "tags"}, files.stream(),
                List.of(TaggedFile::file,
                        file -> file
                                .tags()
                                .stream().map(Tag::fullName)
                                .collect(Collectors.joining(", "))
                )
        );

        System.out.println();
    }
}

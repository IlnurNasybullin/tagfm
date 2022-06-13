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
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.cli.format.TableFormatPrinter;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileSearcher;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "search",
        headerHeading = "Usage:%n%n",
        header = "Searching files by tags",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = """
        searching files by tags with considering the relationship between tags. Searching expression is a boolean \
        expression where value are tags' names. Another legal symbols in expressions are '&' (and), '|' (or), '~' (not) \
        '(', ')' (brackets for priority). IN CURRENT VERSION any terms (tags' names and symbols) must be separated with \
        a space.
        In searching can be considered the next relationships between tags: \
            * SYNONYM - if tag X is synonym tag Y then tag X in expression can be replaced (associated) with tag Y; \
            * HIERARCHY - if tag X is child for tag Y then tag X in expression can be replaced (associated) with tag Y; \
        By default (strategy SIMPLE), the relationships between tags aren't considered.
        """
)
public class FileSearchCommand implements Runnable {

    @CommandLine.Parameters(arity = "1", index = "0", description = "boolean expression for searching")
    private String expression;

    @CommandLine.Option(
            names = {"-fss", "--file-search-strategy"},
            paramLabel = "file searching strategy",
            description = "file searching strategy, default is ${DEFAULT-VALUE}. Valid strategies: ${COMPLETION-CANDIDATES}"
    )
    private FileSearchStrategy fileSearchStrategy = FileSearchStrategy.SIMPLE;

    @CommandLine.Mixin
    private HelpOption helper;

    private final FileManagerCommand fileManager;

    public FileSearchCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        FileSearcher factory = new FileSearcher(namespace);

        Set<TaggedFileView> files = factory.searchFiles(expression, fileSearchStrategy);
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

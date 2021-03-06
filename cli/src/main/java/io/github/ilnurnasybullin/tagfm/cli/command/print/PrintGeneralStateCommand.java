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

package io.github.ilnurnasybullin.tagfm.cli.command.print;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.cli.format.TableFormatPrinter;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Singleton
@CommandLine.Command(
        name = "general-state",
        headerHeading = "Usage:%n%n",
        header = "Working namespace's info printing",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "printing general state about working namespace"
)
public class PrintGeneralStateCommand implements Runnable {

    private final int headerLength = 100;
    private final FileManagerCommand fileManager;

    @CommandLine.Mixin
    private HelpOption helper;

    public PrintGeneralStateCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        System.out.println("Namespace general state:");

        printNamespaceAttributes(namespace);
        printNamespaceTags(namespace);
        printNamespaceSynonyms(namespace);
        printNamespaceFiles(namespace);
    }

    private void printNamespaceFiles(NamespaceView namespace) {
        printHeader("Namespace tagged files:");
        TableFormatPrinter printer = new TableFormatPrinter("%50s | %50s%n", 103);
        printer.print(new String[]{"name", "tags"}, namespace.files().stream(),
                List.of(TaggedFileView::file,
                        file -> file
                            .tags()
                            .stream().map(TagView::fullName)
                            .collect(Collectors.joining(", "))
                )
        );

        System.out.println();
    }

    private void printNamespaceSynonyms(NamespaceView namespace) {
        printHeader("Namespace synonyms:");
        TableFormatPrinter printer = new TableFormatPrinter("%10s | %50s\n", 63);

        AtomicInteger counter = new AtomicInteger(1);
        printer.print(new String[]{"No. class", "tags"}, namespace.synonymGroups().stream(),
                List.of(groups -> counter.getAndIncrement(), groups -> groups.tags()
                        .stream()
                        .map(TagView::fullName)
                        .collect(Collectors.joining(", "))
                )
        );
        System.out.println();
    }

    private void printNamespaceTags(NamespaceView namespace) {
        printHeader("Namespace tags:");
        TableFormatPrinter printer = new TableFormatPrinter("%25s | %40s | %25s%n", 96);
        printer.print(new String[]{"name", "fullName", "parent"}, namespace.tags(false),
                List.of(TagView::name, TagView::fullName, tag -> tag
                        .parent()
                        .map(TagView::name)
                        .orElse("")
                )
        );
        System.out.println();
    }

    private void printNamespaceAttributes(NamespaceView namespace) {
        printHeader("Namespace attributes");
        System.out.printf("\tname: %s%n", namespace.name());
        System.out.printf("\tcreated: %s%n", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(namespace.created()));
        System.out.printf("\tfile naming: %s%n", namespace.fileNaming().name());
        System.out.println();
    }
    
    private void printHeader(String header) {
        int strLength = header.length();
        int beforeLength = (headerLength - strLength) / 2;
        int afterLength = headerLength - strLength - beforeLength;

        String before = "-".repeat(beforeLength);
        String after = "-".repeat(afterLength);
        System.out.printf("%s%s%s%n", before, header, after);
    }
}

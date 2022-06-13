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
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileManager;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "tags",
        headerHeading = "Usage:%n%n",
        header = "Printing of file tags",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "print list of file tags"
)
public class ListFileTagsCommand implements Runnable {

    @CommandLine.Parameters(arity = "0", index = "0", description = "file (if not defining all tags will print)")
    private Path file;

    private final FileManagerCommand fileManager;

    public ListFileTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();

        if (file == null) {
            TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE.iterator(namespace.root(), tag -> tag.children().values())
                    .forEachRemaining(tag -> System.out.println(tag.fullName()));
            return;
        }

        FileManager fileFinder = FileManager.of(namespace);
        TaggedFileView searchedFile = fileFinder.findExact(file);

        searchedFile.tags()
                .stream()
                .map(TagView::fullName)
                .forEach(System.out::println);
    }
}

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

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "files")
public class ListFilesCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-fns", "--file-naming-strategy"})
    private FileNamingStrategy strategy = FileNamingStrategy.ABSOLUTE;

    public ListFilesCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Path currentPath = Path.of(".").toAbsolutePath();

        UnaryOperator<Path> fileMapping = strategy ==
                FileNamingStrategy.ABSOLUTE ?
                UnaryOperator.identity() :
                currentPath::relativize;

        fileManager.namespaceOrThrow()
                .files()
                .stream()
                .map(TaggedFile::file)
                .map(fileMapping)
                .sorted()
                .forEach(System.out::println);
    }
}

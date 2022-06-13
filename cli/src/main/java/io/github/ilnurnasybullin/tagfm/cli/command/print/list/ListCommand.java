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

import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import jakarta.inject.Singleton;
import picocli.CommandLine;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "list",
        subcommands = {
            ListFilesCommand.class,
            ListFileTagsCommand.class,
            ListChildTagsCommand.class,
            ListSynonymsCommand.class,
            ListNamespaces.class
        },
        description = "print list of files, tags, namespaces, synonyms etc."
)
public class ListCommand implements Runnable {

    @CommandLine.Mixin
    private HelpOption helper;

    @Override
    public void run() {

    }
}

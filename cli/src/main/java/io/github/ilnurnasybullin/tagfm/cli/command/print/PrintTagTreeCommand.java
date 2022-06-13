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
import io.github.ilnurnasybullin.tagfm.cli.format.TreeTagPrinter;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Optional;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "tag-tree",
        headerHeading = "Usage:%n%n",
        header = "Visualization of tree tags",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "tree tag visualization"
)
public class PrintTagTreeCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"}, description = "searching root tag by short name")
    private boolean shortName;

    @CommandLine.Parameters(
            arity = "0",
            index = "0",
            description = "root tag (if not defined then using namespace system root tag)"
    )
    private String rootTag;

    @CommandLine.Mixin
    private HelpOption helper;

    public PrintTagTreeCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TagView root = getTag(namespace, rootTag);
        TreeTagPrinter printer = new TreeTagPrinter(root);
        printer.print();
    }

    private TagView getTag(NamespaceView namespace, String tagName) {
        if (tagName == null) {
            return getRootTag(namespace);
        }

        TagService tagService = TagService.of(namespace);
        return shortName ?
                tagService.findByNameExact(tagName) :
                tagService.findByFullNameExact(tagName);
    }

    private TagView getRootTag(NamespaceView namespace) {
        return namespace.root();
    }
}

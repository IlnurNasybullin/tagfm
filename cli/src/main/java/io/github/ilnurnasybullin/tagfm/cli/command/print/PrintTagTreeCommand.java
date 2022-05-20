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
import io.github.ilnurnasybullin.tagfm.cli.format.TreeTagPrinter;
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Optional;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "tag-tree")
public class PrintTagTreeCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Parameters(arity = "0", index = "0")
    private Optional<String> rootTag;

    public PrintTagTreeCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();
        Tag root = rootTag.map(tag -> getTag(namespace, tag)).orElse(getRootTag(namespace));
        TreeTagPrinter printer = new TreeTagPrinter(root);
        printer.print();
    }

    private Tag getTag(Namespace namespace, String tagName) {
        return new NamespaceTagSearcherFacade().searchTag(tagName, namespace, shortName);
    }

    private Tag getRootTag(Namespace namespace) {
        return namespace.root();
    }
}

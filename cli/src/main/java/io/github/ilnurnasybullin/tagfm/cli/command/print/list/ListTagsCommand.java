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
import io.github.ilnurnasybullin.tagfm.cli.util.NamespaceTagSearcherFacade;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(name = "tags")
public class ListTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(index = "0", arity = "0")
    private Optional<String> rootTag;

    @CommandLine.Option(names = {"-sn", "--short-name"})
    private boolean shortName;

    @CommandLine.Option(names = {"-d", "--depth"})
    private Optional<Integer> depth;

    public ListTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        Namespace namespace = fileManager.namespaceOrThrow();
        Tag root = rootTag.map(tag -> getTag(namespace, tag)).orElse(getRootTag(namespace));
        Function<Tag, Collection<Tag>> leafsSupplier = tag -> new TreeMap<String, Tag>(tag.children()).values();

        Iterator<Tag> iterator = depth.map(d ->
                        TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.LEVELED.iterator(root, leafsSupplier, d))
                        .orElse(TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE.iterator(root, leafsSupplier));

        iterator.next();
        iterator.forEachRemaining(tag -> System.out.println(tag.fullName()));
    }

    private Tag getTag(Namespace namespace, String tagName) {
        return new NamespaceTagSearcherFacade().searchTag(tagName, namespace, shortName);
    }

    private Tag getRootTag(Namespace namespace) {
        return namespace.root();
    }

}
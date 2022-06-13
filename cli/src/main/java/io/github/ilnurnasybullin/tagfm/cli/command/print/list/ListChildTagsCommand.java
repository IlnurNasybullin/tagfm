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
import io.github.ilnurnasybullin.tagfm.cli.command.mixin.HelpOption;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.TagService;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
@Singleton
@CommandLine.Command(
        name = "child-tags",
        headerHeading = "Usage:%n%n",
        header = "Printing of child tags",
        synopsisHeading = "%n",
        parameterListHeading = "Parameters:%n",
        description = "print list of child tags"
)
public class ListChildTagsCommand implements Runnable {

    private final FileManagerCommand fileManager;

    @CommandLine.Parameters(index = "0", arity = "0", description = "root tag")
    private String rootTag;

    @CommandLine.Option(names = {"-sn", "--short-name"}, description = "using short name for searching root tag")
    private boolean shortName;

    @CommandLine.Option(
            names = {"-d", "--depth"},
            description = "depth of tree for searching child tags (by default searching all children of root tag)"
    )
    private Integer depth;

    @CommandLine.Mixin
    private HelpOption helper;

    public ListChildTagsCommand(FileManagerCommand fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void run() {
        NamespaceView namespace = fileManager.namespaceOrThrow();
        TagView root = getTag(namespace, rootTag);
        Function<TagView, Collection<TagView>> leafsSupplier = tag -> new TreeMap<String, TagView>(tag.children()).values();

        Iterator<TagView> iterator = depth == null ?
                        TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE.iterator(root, leafsSupplier) :
                        TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.LEVELED.iterator(root, leafsSupplier, depth);

        iterator.next();
        iterator.forEachRemaining(tag -> System.out.println(tag.fullName()));
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

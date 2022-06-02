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

package io.github.ilnurnasybullin.tagfm.repository.xml.dto;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.SynonymGroupEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.TagEntity;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class NamespaceDto implements NamespaceEntity {

    private final Namespace namespace;
    private final Tag root;

    private NamespaceDto(Namespace namespace, Tag root) {
        this.namespace = namespace;
        this.root = root;
    }

    public static NamespaceDto singleRoot(Namespace namespace) {
        renameFiles(namespace);
        Tag root = createRoot(namespace.getTags());
        return new NamespaceDto(namespace, root);
    }

    private static Tag createRoot(Set<Tag> tags) {
        Tag root = new Tag();
        tags.stream()
                .filter(tag -> tag.getParent() == null)
                .forEach(tag -> root.children().add(tag));

        return root;
    }

    private static void renameFiles(io.github.ilnurnasybullin.tagfm.repository.xml.entity.Namespace namespace) {
        for (TaggedFile taggedFile: namespace.getTaggedFiles()) {
            try {
                taggedFile.setName(Paths.get(taggedFile.getName()).toRealPath().toString());
            } catch (IOException e) {
                throw new UncheckedIOException(
                        String.format("Exception with reading file [%s] URL", taggedFile.getName()), e
                );
            }
        }
    }

    @Override
    public String name() {
        return namespace.getName();
    }

    @Override
    public ZonedDateTime created() {
        return namespace.created();
    }

    @Override
    public FileNamingStrategy fileNaming() {
        return FileNamingStrategyEntity.to(namespace.getFileNaming());
    }

    @Override
    public TagEntity root() {
        return root;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<TaggedFile> files() {
        return namespace.getTaggedFiles();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SynonymGroup> synonymGroups() {
        return namespace.getSynonymGroups();
    }
}

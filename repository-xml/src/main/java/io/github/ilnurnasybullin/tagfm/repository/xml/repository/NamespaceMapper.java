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

package io.github.ilnurnasybullin.tagfm.repository.xml.repository;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.TagEntity;
import io.github.ilnurnasybullin.tagfm.core.repository.TaggedFileEntity;
import io.github.ilnurnasybullin.tagfm.repository.xml.dto.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.repository.xml.entity.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NamespaceMapper {

    private final AtomicLong idGenerator;

    public NamespaceMapper() {
        idGenerator = new AtomicLong();
    }

    public Namespace from(NamespaceEntity namespace) {
        Namespace entity = new Namespace();
        entity.setCreated(namespace.created());
        entity.setName(namespace.name());

        Map<TagEntity, Tag> tagsMap = namespace.tags(false)
                .collect(Collectors.toMap(Function.identity(), this::from));

        //parent binding
        tagsMap.forEach((tag, tagEntity) -> tag.parent()
                .ifPresent(tagParent -> tagEntity.setParent(tagsMap.get(tagParent))));

        entity.setTags(Set.copyOf(tagsMap.values()));

        List<SynonymGroup> synonymEntities = namespace.synonymGroups()
                .stream()
                .map(group -> {
                    SynonymGroup synonymGroup = new SynonymGroup();
                    synonymGroup.setTags(group.tags()
                            .stream()
                            .map(tagsMap::get)
                            .collect(Collectors.toUnmodifiableSet()));
                    return synonymGroup;
                }).toList();
        entity.setSynonymGroups(synonymEntities);

        FileNamingStrategy strategy = namespace.fileNaming();
        entity.setFileNaming(FileNamingStrategyEntity.from(strategy));

        Path currentPath;
        try {
            currentPath = Path.of(".").toAbsolutePath().toRealPath();
        } catch (IOException e) {
            // this is never thrown
            throw new UncheckedIOException("Problem with reading current directory's url", e);
        }

        Function<Path, String> naming = switch (strategy) {
            case ABSOLUTE -> Path::toString;
            case RELATIVE ->  filePath -> currentPath.relativize(filePath.toAbsolutePath()).toString();
        };

        Set<TaggedFile> taggedFiles = namespace.files()
                .stream()
                .map(taggedFile -> from(taggedFile, tagsMap, naming))
                .collect(Collectors.toSet());

        entity.setTaggedFiles(taggedFiles);

        return entity;
    }

    private TaggedFile from(TaggedFileEntity taggedFile, Map<TagEntity, Tag> tagsMap, Function<Path, String> naming) {
        TaggedFile entity = new TaggedFile();

        try {
            String fileName = naming.apply(taggedFile.file().toRealPath());
            entity.setName(fileName);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Invalid file's url [%s]", taggedFile.file()), e);
        }

        Set<Tag> tags = taggedFile.tags()
                .stream()
                .map(tagsMap::get)
                .collect(Collectors.toSet());
        entity.setTags(tags);

        return entity;
    }

    private Tag from(TagEntity tag) {
        Tag tagEntity = Tag.createWithId(idGenerator.getAndIncrement());
        tagEntity.setName(tag.name());
        return tagEntity;
    }

    public NamespaceEntity to(Namespace entity) {
        return NamespaceDto.singleRoot(entity);
    }
}

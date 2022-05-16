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
import io.github.ilnurnasybullin.tagfm.core.repository.Namespace;
import io.github.ilnurnasybullin.tagfm.core.repository.Tag;
import io.github.ilnurnasybullin.tagfm.core.repository.TaggedFile;
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

    public NamespaceEntity from(Namespace namespace) {
        NamespaceEntity entity = new NamespaceEntity();
        entity.setCreated(namespace.created());
        entity.setName(namespace.name());

        Map<Tag, TagEntity> tagsMap = namespace.tags()
                .collect(Collectors.toMap(Function.identity(), this::from));

        tagsMap.forEach((tag, tagEntity) -> tag.parent()
                .ifPresent(tagParent -> tagEntity.setParent(tagsMap.get(tagParent))));
        entity.setTags(Set.copyOf(tagsMap.values()));

        List<SynonymsEntity> synonymEntities = namespace.synonyms()
                .stream()
                .map(synonymsSet -> synonymsSet.stream().map(tagsMap::get).collect(Collectors.toSet()))
                .map(synonymsSet -> {
                    SynonymsEntity synonyms = new SynonymsEntity();
                    synonyms.setTags(synonymsSet);
                    return synonyms;
                }).toList();
        entity.setSynonyms(synonymEntities);

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

        Set<TaggedFileEntity> taggedFiles = namespace.files()
                .stream()
                .map(taggedFile -> from(taggedFile, tagsMap, naming))
                .collect(Collectors.toSet());

        entity.setTaggedFiles(taggedFiles);

        return entity;
    }

    private TaggedFileEntity from(TaggedFile taggedFile, Map<Tag, TagEntity> tagsMap, Function<Path, String> naming) {
        TaggedFileEntity entity = new TaggedFileEntity();

        try {
            String fileName = naming.apply(taggedFile.file().toRealPath());
            entity.setName(fileName);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Invalid file's url [%s]", taggedFile.file()), e);
        }

        Set<TagEntity> tags = taggedFile.<Tag>tags()
                .stream()
                .map(tagsMap::get)
                .collect(Collectors.toSet());
        entity.setTags(tags);

        return entity;
    }

    private TagEntity from(Tag tag) {
        TagEntity tagEntity = TagEntity.createWithId(idGenerator.getAndIncrement());
        tagEntity.setName(tag.name());
        return tagEntity;
    }

    public Namespace to(NamespaceEntity entity) {
        return NamespaceDto.singleRoot(entity);
    }
}

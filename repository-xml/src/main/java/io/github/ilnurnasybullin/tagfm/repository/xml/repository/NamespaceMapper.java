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
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepoDto;
import io.github.ilnurnasybullin.tagfm.core.repository.TagRepoDto;
import io.github.ilnurnasybullin.tagfm.core.repository.TaggedFileRepoDto;
import io.github.ilnurnasybullin.tagfm.repository.xml.dto.Namespace;
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

    public NamespaceEntity from(NamespaceRepoDto namespaceRepoDto) {
        NamespaceEntity entity = new NamespaceEntity();
        entity.setCreated(namespaceRepoDto.created());
        entity.setName(namespaceRepoDto.name());

        Map<TagRepoDto, TagEntity> tagsMap = namespaceRepoDto.tags(false)
                .collect(Collectors.toMap(Function.identity(), this::from));

        tagsMap.forEach((tag, tagEntity) -> tag.parent()
                .ifPresent(tagParent -> tagEntity.setParent(tagsMap.get(tagParent))));
        entity.setTags(Set.copyOf(tagsMap.values()));

        List<SynonymsEntity> synonymEntities = namespaceRepoDto.synonyms()
                .stream()
                .map(synonymsSet -> synonymsSet.stream().map(tagsMap::get).collect(Collectors.toSet()))
                .map(synonymsSet -> {
                    SynonymsEntity synonyms = new SynonymsEntity();
                    synonyms.setTags(synonymsSet);
                    return synonyms;
                }).toList();
        entity.setSynonyms(synonymEntities);

        FileNamingStrategy strategy = namespaceRepoDto.fileNaming();
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

        Set<TaggedFileEntity> taggedFiles = namespaceRepoDto.files()
                .stream()
                .map(taggedFile -> from(taggedFile, tagsMap, naming))
                .collect(Collectors.toSet());

        entity.setTaggedFiles(taggedFiles);

        return entity;
    }

    private TaggedFileEntity from(TaggedFileRepoDto taggedFile, Map<TagRepoDto, TagEntity> tagsMap, Function<Path, String> naming) {
        TaggedFileEntity entity = new TaggedFileEntity();

        try {
            String fileName = naming.apply(taggedFile.file().toRealPath());
            entity.setName(fileName);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Invalid file's url [%s]", taggedFile.file()), e);
        }

        Set<TagEntity> tags = taggedFile.<TagRepoDto>tags()
                .stream()
                .map(tagsMap::get)
                .collect(Collectors.toSet());
        entity.setTags(tags);

        return entity;
    }

    private TagEntity from(TagRepoDto tagRepoDto) {
        TagEntity tagEntity = TagEntity.createWithId(idGenerator.getAndIncrement());
        tagEntity.setName(tagRepoDto.name());
        return tagEntity;
    }

    public NamespaceRepoDto to(NamespaceEntity entity) {
        return Namespace.singleRoot(entity);
    }
}

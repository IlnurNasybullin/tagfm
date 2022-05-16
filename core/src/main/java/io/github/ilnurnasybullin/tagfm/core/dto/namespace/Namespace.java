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

package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileManager;
import io.github.ilnurnasybullin.tagfm.core.dto.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.repository.Tag;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Namespace extends NamespaceValidator {

    protected Namespace(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                        SynonymTagManager synonymsManager, TaggedFileManager fileManager) {
        super(name, created, fileNaming, ROOT, synonymsManager, fileManager);
    }

    static NamespaceDto from(io.github.ilnurnasybullin.tagfm.core.repository.Namespace namespace) {
        TreeTagDto ROOT = TreeTag.root();
        Map<Tag, TreeTagDto> tagsMap = new HashMap<>();
        namespace.tags()
                .forEach(tag -> {
                    TreeTagDto parent = tag.parent()
                            .map(tagsMap::get)
                            .orElse(ROOT);
                    TreeTagDto tagDto = TreeTag.initWithParent(tag.name(), parent);
                    tagsMap.put(tag, tagDto);
                });

        List<Set<TreeTagDto>> synonyms = namespace.synonyms()
                .stream()
                .map(tags -> tags.stream()
                        .map(tagsMap::get)
                        .collect(
                                () -> Collections.<TreeTagDto>newSetFromMap(new IdentityHashMap<>()),
                                Set::add,
                                Set::addAll
                        ))
                .collect(Collectors.toList());

        Set<TaggedFileDto> files = namespace.files().stream().map(file -> {
            Set<TreeTagDto> tags = file.<Tag>tags()
                    .stream()
                    .map(tagsMap::get)
                    .collect(
                            () -> Collections.newSetFromMap(new IdentityHashMap<>()),
                            Set::add,
                            Set::addAll
                    );
            return TaggedFile.initWithTags(file.file(), tags);
        }).collect(Collectors.toSet());

        return new Namespace(namespace.name(), namespace.created(), namespace.fileNaming(), ROOT,
                SynonymTagManager.of(synonyms), TaggedFileManager.of(files));
    }

    static NamespaceDto init(String name, FileNamingStrategy fileNaming) {
        ZonedDateTime now = LocalDateTime.now(Clock.systemUTC())
                .atZone(ZoneId.of("UTC"));

        return new Namespace(name, now, fileNaming, TreeTag.root(),
                SynonymTagManager.of(), TaggedFileManager.of());
    }
}

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

package io.github.ilnurnasybullin.tagfm.core.repository;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface NamespaceRepoDto {
    String name();
    ZonedDateTime created();

    FileNamingStrategy fileNaming();

    <T extends TaggedFileRepoDto> Set<T> files();
    <T extends TagRepoDto> List<Set<T>> synonyms();

    TagRepoDto root();

    default Stream<TagRepoDto> tags(boolean withRoot) {
        Iterator<TagRepoDto> iterator = TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE
                .iterator(root(), TagRepoDto::children);
        if (!withRoot) {
            iterator.next();
        }

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                Spliterator.DISTINCT | Spliterator.NONNULL
        ), false);
    }
}

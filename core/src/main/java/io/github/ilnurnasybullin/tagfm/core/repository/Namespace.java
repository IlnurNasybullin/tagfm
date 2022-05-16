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

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Namespace {
    String name();
    ZonedDateTime created();

    FileNamingStrategy fileNaming();

    <T extends TaggedFile> Set<T> files();
    <T extends Tag> Iterator<T> horizontalTraversal();

    <T extends Tag> List<Set<T>> synonyms();

    default <T extends Tag> Stream<T> tags() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                horizontalTraversal(),
                Spliterator.DISTINCT | Spliterator.NONNULL
        ), false);
    }

}

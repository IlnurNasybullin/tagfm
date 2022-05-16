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

package io.github.ilnurnasybullin.tagfm.cli.util;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceFileManager;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceNotExistTaggedFileException;
import io.github.ilnurnasybullin.tagfm.core.repository.Tag;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class NamespaceFileManagerFacade {

    public Stream<TaggedFileDto> findOrCreate(Collection<Path> files, NamespaceDto namespace) {
        return NamespaceFileManager.of(namespace)
                .findOrCreate(files)
                .values()
                .stream();
    }

    public TaggedFileDto findOrCreate(Path file, NamespaceDto namespace) {
        return NamespaceFileManager.of(namespace)
                .findOrCreate(file);
    }


    public Stream<TaggedFileDto> find(Collection<Path> files, NamespaceDto namespace) {
        return NamespaceFileManager.of(namespace)
                .find(files)
                .values()
                .stream();
    }

    public Optional<TaggedFileDto> find(Path file, NamespaceDto namespace) {
        return NamespaceFileManager.of(namespace).find(file);
    }

    public TaggedFileDto findExact(Path file, NamespaceDto namespace) {
        return find(file, namespace)
                .orElseThrow(() ->
                        new NamespaceNotExistTaggedFileException(
                                String.format("File or directory [%s] is not exist in namespace [%s]",
                                        file, namespace.name()
                                )
                        )
                );
    }
}

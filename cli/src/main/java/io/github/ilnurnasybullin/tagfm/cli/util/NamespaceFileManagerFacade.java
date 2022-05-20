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

import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.api.service.FileFinderManager;
import io.github.ilnurnasybullin.tagfm.core.api.service.NamespaceNotExistTaggedFileException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class NamespaceFileManagerFacade {

    public Stream<TaggedFile> findOrCreate(Collection<Path> files, Namespace namespace) {
        return FileFinderManager.of(namespace)
                .findOrCreate(files)
                .values()
                .stream();
    }

    public TaggedFile findOrCreate(Path file, Namespace namespace) {
        return FileFinderManager.of(namespace)
                .findOrCreate(file);
    }


    public Stream<TaggedFile> find(Collection<Path> files, Namespace namespace) {
        return FileFinderManager.of(namespace)
                .find(files)
                .values()
                .stream();
    }

    public Optional<TaggedFile> find(Path file, Namespace namespace) {
        return FileFinderManager.of(namespace).find(file);
    }

    public TaggedFile findExact(Path file, Namespace namespace) {
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

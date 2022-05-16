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
import io.github.ilnurnasybullin.tagfm.api.service.NamespaceService;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

import java.util.Optional;

public class NamespaceServiceImpl implements NamespaceService<NamespaceDto> {

    private final NamespaceRepository repository;

    public NamespaceServiceImpl(NamespaceRepository repository) {
        this.repository = repository;
    }

    public NamespaceServiceImpl() {
        this(NamespaceRepository.get());
    }

    public void commit(NamespaceDto namespace) {
        repository.commit(namespace);
    }

    public Optional<NamespaceDto> find() {
        return repository.findBy()
                .map(Namespace::from);
    }

    @Override
    public NamespaceDto init(String name, FileNamingStrategy strategy) {
        find().ifPresent(namespace -> {
            throw new NamespaceAlreadyInitialized(String.format("Namespace [%s] already initialized!", namespace.name()));
        });

        return Namespace.init(name, strategy);
    }
}

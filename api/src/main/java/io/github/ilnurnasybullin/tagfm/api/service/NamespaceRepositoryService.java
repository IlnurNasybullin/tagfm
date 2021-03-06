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

package io.github.ilnurnasybullin.tagfm.api.service;

import java.util.List;
import java.util.Optional;

public interface NamespaceRepositoryService<N> {
    N init(String name, FileNamingStrategy strategy);
    Optional<N> find(String name);
    List<N> getAll();
    void commit(N namespace);
    void replace(String name, N namespace);
    void remove(N namespace);

    Optional<N> getWorkingNamespace();
    void setWorkingNamespace(String name);
}

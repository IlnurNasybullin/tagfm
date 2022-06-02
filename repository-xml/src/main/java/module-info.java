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

import io.github.ilnurnasybullin.tagfm.repository.xml.repository.NamespaceRepositoryImpl;
import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;

module tagfm.repository.xml {
    requires tagfm.core;

    requires java.activation;
    requires java.xml.bind;
    requires com.sun.xml.bind;

    exports io.github.ilnurnasybullin.tagfm.repository.xml.repository to java.xml.bind;
    exports io.github.ilnurnasybullin.tagfm.repository.xml.entity to java.xml.bind;

    provides NamespaceRepository with NamespaceRepositoryImpl;

    opens io.github.ilnurnasybullin.tagfm.repository.xml.entity to java.xml.bind;
    exports io.github.ilnurnasybullin.tagfm.repository.xml.fileManager to java.xml.bind;
}
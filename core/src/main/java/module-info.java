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

import io.github.ilnurnasybullin.tagfm.core.repository.NamespaceRepository;
import io.github.ilnurnasybullin.tagfm.core.evaluator.BooleanExpressionParser;

module tagfm.core {
    requires transitive tagfm.api;

    exports io.github.ilnurnasybullin.tagfm.core.api.dto to tagfm.cli;
    exports io.github.ilnurnasybullin.tagfm.core.api.service to tagfm.cli;

    exports io.github.ilnurnasybullin.tagfm.core.evaluator to tagfm.bool.expression.spi.provider;
    exports io.github.ilnurnasybullin.tagfm.core.repository to tagfm.repository.xml;

    exports io.github.ilnurnasybullin.tagfm.core.util.iterator;

    uses NamespaceRepository;
    uses BooleanExpressionParser;
}
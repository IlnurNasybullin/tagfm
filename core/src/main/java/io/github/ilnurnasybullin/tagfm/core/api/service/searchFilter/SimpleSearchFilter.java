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

package io.github.ilnurnasybullin.tagfm.core.api.service.searchFilter;

import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.parser.LogicalExpressionEvaluator;

import java.util.Map;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public class SimpleSearchFilter implements TaggedFilesFilter {

    private final LogicalExpressionEvaluator<Tag> evaluator;

    private SimpleSearchFilter(LogicalExpressionEvaluator<Tag> evaluator) {
        this.evaluator = evaluator;
    }

    public static TaggedFilesFilter of(Namespace namespace,
                                       LogicalExpressionEvaluator<String> expressionEvaluator,
                                       Map<String, Tag> usedTags) {
        LogicalExpressionEvaluator<Tag> tagsEvaluator = expressionEvaluator.map(usedTags::get);
        return new SimpleSearchFilter(tagsEvaluator);
    }

    @Override
    public boolean test(TaggedFile file) {
        Set<Tag> tags = file.tags();
        return evaluator.evaluate(tags::contains);
    }
}
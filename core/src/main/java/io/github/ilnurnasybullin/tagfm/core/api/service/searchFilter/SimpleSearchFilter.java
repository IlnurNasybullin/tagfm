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

import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.parser.LogicalExpressionEvaluator;

import java.util.Map;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public class SimpleSearchFilter implements TaggedFilesFilter {

    private final LogicalExpressionEvaluator<TagView> evaluator;

    private SimpleSearchFilter(LogicalExpressionEvaluator<TagView> evaluator) {
        this.evaluator = evaluator;
    }

    public static TaggedFilesFilter of(NamespaceView namespace,
                                       LogicalExpressionEvaluator<String> expressionEvaluator,
                                       Map<String, TagView> usedTags) {
        LogicalExpressionEvaluator<TagView> tagsEvaluator = expressionEvaluator.map(usedTags::get);
        return new SimpleSearchFilter(tagsEvaluator);
    }

    @Override
    public boolean test(TaggedFileView file) {
        Set<TagView> tags = file.tags();
        return evaluator.evaluate(tags::contains);
    }
}

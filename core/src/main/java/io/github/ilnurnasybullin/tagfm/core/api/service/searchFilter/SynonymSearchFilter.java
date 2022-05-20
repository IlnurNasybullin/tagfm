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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class SynonymSearchFilter implements TaggedFilesFilter {

    private final Map<Tag, Object> synonymsClass;
    private final LogicalExpressionEvaluator<Object> evaluator;

    private SynonymSearchFilter(Map<Tag, Object> synonymsClass,
                                LogicalExpressionEvaluator<Object> evaluator) {
        this.synonymsClass = synonymsClass;
        this.evaluator = evaluator;
    }

    public static TaggedFilesFilter of(Namespace namespace,
                                LogicalExpressionEvaluator<String> expressionEvaluator,
                                Map<String, Tag> usedTags) {
        Map<Tag, Object> synonyms = new HashMap<>(namespace.synonymsManager().synonymMap());
        usedTags.values()
                .stream()
                .filter(tag -> !synonyms.containsKey(tag))
                .forEach(tag -> synonyms.put(tag, tag));

        LogicalExpressionEvaluator<Object> evaluator =
                expressionEvaluator.map(tagName -> synonyms.get(usedTags.get(tagName)));

        return new SynonymSearchFilter(synonyms, evaluator);
    }

    @Override
    public boolean test(TaggedFile file) {
        Set<Object> synonyms = file.tags()
                .stream()
                .map(synonymsClass::get)
                .collect(Collectors.toSet());

        return evaluator.evaluate(synonyms::contains);
    }
}

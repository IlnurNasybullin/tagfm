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

package io.github.ilnurnasybullin.tagfm.core.search;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class SynonymSearchFilter implements TaggedFilesFilter {

    private final Map<TreeTagDto, Object> synonymsClass;
    private final LogicalExpressionEvaluator<Object> evaluator;

    private SynonymSearchFilter(Map<TreeTagDto, Object> synonymsClass,
                                LogicalExpressionEvaluator<Object> evaluator) {
        this.synonymsClass = synonymsClass;
        this.evaluator = evaluator;
    }

    static TaggedFilesFilter of(NamespaceDto namespace,
                                       LogicalExpressionEvaluator<String> expressionEvaluator,
                                       Map<String, TreeTagDto> usedTags) {
        Map<TreeTagDto, Object> synonyms = new HashMap<>(namespace.synonymsManager().synonymMap());
        usedTags.values()
                .stream()
                .filter(tag -> !synonyms.containsKey(tag))
                .forEach(tag -> synonyms.put(tag, tag));

        LogicalExpressionEvaluator<Object> evaluator =
                expressionEvaluator.map(tagName -> synonyms.get(usedTags.get(tagName)));

        return new SynonymSearchFilter(synonyms, evaluator);
    }

    @Override
    public boolean test(TaggedFileDto taggedFile) {
        Set<Object> synonyms = taggedFile.tags()
                .stream()
                .map(synonymsClass::get)
                .collect(Collectors.toSet());

        return evaluator.evaluate(synonyms::contains);
    }
}

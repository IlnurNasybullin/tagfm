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
import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymGroup;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.parser.LogicalExpressionEvaluator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class SynonymSearchFilter implements TaggedFilesFilter {

    private final Map<TagView, SynonymGroup> synonymsClass;
    private final LogicalExpressionEvaluator<SynonymGroup> evaluator;

    private SynonymSearchFilter(Map<TagView, SynonymGroup> synonymsClass,
                                LogicalExpressionEvaluator<SynonymGroup> evaluator) {
        this.synonymsClass = synonymsClass;
        this.evaluator = evaluator;
    }

    public static TaggedFilesFilter of(NamespaceView namespace,
                                       LogicalExpressionEvaluator<String> expressionEvaluator,
                                       Map<String, TagView> usedTags) {
        Map<TagView, SynonymGroup> synonyms = new HashMap<>(namespace.synonymsManager().synonymMap());
        usedTags.values()
                .stream()
                .filter(tag -> !synonyms.containsKey(tag))
                .forEach(tag -> synonyms.put(tag, new SynonymGroup(List.of((TreeTag) tag))));

        LogicalExpressionEvaluator<SynonymGroup> evaluator =
                expressionEvaluator.map(tagName -> synonyms.get(usedTags.get(tagName)));

        return new SynonymSearchFilter(synonyms, evaluator);
    }

    @Override
    public boolean test(TaggedFileView file) {
        Set<Object> synonyms = file.tags()
                .stream()
                .map(synonymsClass::get)
                .collect(Collectors.toSet());

        return evaluator.evaluate(synonyms::contains);
    }
}

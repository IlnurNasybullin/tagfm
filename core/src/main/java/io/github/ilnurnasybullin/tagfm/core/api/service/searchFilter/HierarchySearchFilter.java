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
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public class HierarchySearchFilter implements TaggedFilesFilter {

    private final LogicalExpressionEvaluator<TagView> evaluator;

    private HierarchySearchFilter(LogicalExpressionEvaluator<TagView> evaluator) {
        this.evaluator = evaluator;
    }

    public static TaggedFilesFilter of(NamespaceView namespace,
                                       LogicalExpressionEvaluator<String> evaluator,
                                       Map<String, TagView> usedTags) {
        LogicalExpressionEvaluator<TagView> mappedEvaluator = evaluator.map(usedTags::get);
        return new HierarchySearchFilter(mappedEvaluator);
    }

    @Override
    public boolean test(TaggedFileView file) {
        Set<TagView> tags = file.tags();
        return evaluator.evaluate(tag -> hasChild(tag, tags));
    }

    private boolean hasChild(TagView tag, Set<TagView> tags) {
        Iterator<TagView> iterator = TreeIteratorsFactory.HORIZONTAL_TRAVERSAL
                .SIMPLE
                .iterator(tag, t -> t.children().values());

        boolean isChild = false;
        while (iterator.hasNext() && !isChild) {
            isChild = tags.contains(iterator.next());
        }

        return isChild;
    }
}

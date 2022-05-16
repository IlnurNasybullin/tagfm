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
import io.github.ilnurnasybullin.tagfm.core.iterator.TreeIteratorsFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
class HierarchySearchFilter implements TaggedFilesFilter {

    private final LogicalExpressionEvaluator<TreeTagDto> evaluator;

    private HierarchySearchFilter(LogicalExpressionEvaluator<TreeTagDto> evaluator) {
        this.evaluator = evaluator;
    }

    public static TaggedFilesFilter of(NamespaceDto namespace,
                                       LogicalExpressionEvaluator<String> evaluator,
                                       Map<String, TreeTagDto> usedTags) {
        LogicalExpressionEvaluator<TreeTagDto> mappedEvaluator = evaluator.map(usedTags::get);
        return new HierarchySearchFilter(mappedEvaluator);
    }

    @Override
    public boolean test(TaggedFileDto taggedFile) {
        Set<TreeTagDto> tags = taggedFile.tags();
        return evaluator.evaluate(tag -> hasChild(tag, tags));
    }

    private boolean hasChild(TreeTagDto tag, Set<TreeTagDto> tags) {
        Iterator<TreeTagDto> iterator = TreeIteratorsFactory.HORIZONTAL_TRAVERSAL
                .SIMPLE
                .iterator(tag, t -> t.children().values());

        boolean isChild = false;
        while (iterator.hasNext() && !isChild) {
            isChild = tags.contains(iterator.next());
        }

        return isChild;
    }
}

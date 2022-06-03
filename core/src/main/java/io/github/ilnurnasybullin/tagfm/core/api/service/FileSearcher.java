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

package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.FileSearchStrategy;
import io.github.ilnurnasybullin.tagfm.api.service.NonUniqueTagNameException;
import io.github.ilnurnasybullin.tagfm.api.service.TagNotFoundException;
import io.github.ilnurnasybullin.tagfm.api.service.FileSearchingService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.NamespaceView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.dto.TaggedFileView;
import io.github.ilnurnasybullin.tagfm.core.api.service.searchFilter.HierarchySearchFilter;
import io.github.ilnurnasybullin.tagfm.core.api.service.searchFilter.SimpleSearchFilter;
import io.github.ilnurnasybullin.tagfm.core.api.service.searchFilter.SynonymSearchFilter;
import io.github.ilnurnasybullin.tagfm.core.api.service.searchFilter.TaggedFilesFilter;
import io.github.ilnurnasybullin.tagfm.core.parser.LogicalExpressionEvaluator;
import io.github.ilnurnasybullin.tagfm.core.parser.LogicalExpressionParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class FileSearcher implements FileSearchingService<TaggedFileView> {

    private final NamespaceView namespace;

    public FileSearcher(NamespaceView namespace) {
        this.namespace = namespace;
    }

    @Override
    public Set<TaggedFileView> searchFiles(List<String> expressionTokens, FileSearchStrategy searchStrategy) {
        Map<String, TagView> usedTags = new HashMap<>();

        Map<String, TagView> fullNamesMap = namespace.tags(false)
                .collect(Collectors.toMap(TagView::fullName, Function.identity()));

        Map<String, List<TagView>> shortNamesMap = namespace.tags(false)
                .collect(Collectors.groupingBy(TagView::name));

        Function<String, String> tagsListener = validateListener(usedTags, fullNamesMap, shortNamesMap);

        LogicalExpressionEvaluator<String> evaluator =
                LogicalExpressionParser.<String>get()
                .parse(expressionTokens, tagsListener);

        TaggedFilesFilter filesFilter = switch (searchStrategy) {
            case SIMPLE -> SimpleSearchFilter.of(namespace, evaluator, usedTags);
            case SYNONYMS -> SynonymSearchFilter.of(namespace, evaluator, usedTags);
            case HIERARCHY -> HierarchySearchFilter.of(namespace, evaluator, usedTags);
        };

        return namespace.files()
                .stream()
                .filter(filesFilter)
                .collect(Collectors.toSet());
    }

    private Function<String, String> validateListener(Map<String, TagView> usedTags,
                                                      Map<String, TagView> fullNamesMap,
                                                      Map<String, List<TagView>> shortNamesMap) {
        return tagName -> {
            if (shortNamesMap.containsKey(tagName)) {
                List<TagView> tags = shortNamesMap.get(tagName);
                if (tags.size() > 1) {
                    throw new NonUniqueTagNameException(
                            String.format(
                                    "Tag with name [%s] isn't unique; exist tags [%s] with same name!",
                                    tagName,
                                    tags.stream().map(TagView::fullName).collect(Collectors.joining("; "))
                            )
                    );
                }
                usedTags.put(tagName, tags.get(0));
                return tagName;
            }
            if (fullNamesMap.containsKey(tagName)) {
                usedTags.put(tagName, fullNamesMap.get(tagName));
                return tagName;
            }

            throw new TagNotFoundException(
                    String.format("Tag with name [%s] isn't exist in namespace [%s]", tagName, namespace.name())
            );
        };
    }

}

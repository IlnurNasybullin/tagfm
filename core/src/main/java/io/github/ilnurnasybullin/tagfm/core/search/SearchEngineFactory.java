package io.github.ilnurnasybullin.tagfm.core.search;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.NamespaceNotExistTagsException;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ilnur Nasybullin
 */
public class SearchEngineFactory {

    private final NamespaceDto namespace;

    public SearchEngineFactory(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public Set<TaggedFileDto> searchFiles(List<String> expressionTokens, FileSearchStrategy searchStrategy) {
        Map<String, TreeTagDto> usedTags = new HashMap<>();

        Map<String, TreeTagDto> fullNamesMap = namespace.tags()
                .collect(Collectors.toMap(TreeTagDto::fullName, Function.identity()));

        Map<String, List<TreeTagDto>> shortNamesMap = namespace.tags()
                .collect(Collectors.groupingBy(TreeTagDto::name));

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

    private Function<String, String> validateListener(Map<String, TreeTagDto> usedTags,
                                                      Map<String, TreeTagDto> fullNamesMap,
                                                      Map<String, List<TreeTagDto>> shortNamesMap) {
        return tagName -> {
            if (shortNamesMap.containsKey(tagName)) {
                List<TreeTagDto> tags = shortNamesMap.get(tagName);
                if (tags.size() > 1) {
                    throw new UniquelyIdentifiableTagException(
                            String.format(
                                    "Tag with name [%s] isn't unique; exist tags [%s] with same name!",
                                    tagName,
                                    tags.stream().map(TreeTagDto::fullName).collect(Collectors.joining("; "))
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

            throw new NamespaceNotExistTagsException(
                    String.format("Tag with name [%s] isn't exist in namespace [%s]", tagName, namespace.name())
            );
        };
    }

}

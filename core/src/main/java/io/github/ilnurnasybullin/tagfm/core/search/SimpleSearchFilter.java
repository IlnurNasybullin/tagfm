package io.github.ilnurnasybullin.tagfm.core.search;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.Map;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
class SimpleSearchFilter implements TaggedFilesFilter {

    private final LogicalExpressionEvaluator<TreeTagDto> evaluator;

    private SimpleSearchFilter(LogicalExpressionEvaluator<TreeTagDto> evaluator) {
        this.evaluator = evaluator;
    }

    public static TaggedFilesFilter of(NamespaceDto namespace,
                                       LogicalExpressionEvaluator<String> expressionEvaluator,
                                       Map<String, TreeTagDto> usedTags) {
        LogicalExpressionEvaluator<TreeTagDto> tagsEvaluator = expressionEvaluator.map(usedTags::get);
        return new SimpleSearchFilter(tagsEvaluator);
    }

    @Override
    public boolean test(TaggedFileDto taggedFile) {
        Set<TreeTagDto> tags = taggedFile.tags();
        return evaluator.evaluate(tags::contains);
    }
}

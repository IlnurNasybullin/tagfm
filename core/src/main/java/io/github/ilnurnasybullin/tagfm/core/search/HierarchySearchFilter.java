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

package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.TagIteratorService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;

import java.util.Iterator;

/**
 * @author Ilnur Nasybullin
 */
public class TagIteratorServiceImpl implements TagIteratorService<Tag> {

    private final Tag root;

    private TagIteratorServiceImpl(Tag root) {
        this.root = root;
    }

    public static TagIteratorServiceImpl of(Tag root) {
        return new TagIteratorServiceImpl(root);
    }

    @Override
    public Iterator<Tag> horizontalTraversal() {
        return TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE
                .iterator(root, tag -> tag.children().values());
    }

    @Override
    public Iterator<Tag> leveledIterator(int maxDepth) {
        return TreeIteratorsFactory.HORIZONTAL_TRAVERSAL
                .LEVELED.iterator(root, tag -> tag.children().values(), maxDepth);
    }
}

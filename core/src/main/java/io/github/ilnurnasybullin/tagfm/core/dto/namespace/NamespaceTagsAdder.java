package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.Collection;
import java.util.Map;

public class NamespaceTagsAdder {

    public void addTags(Collection<TreeTagDto> tags, NamespaceDto namespace) {
        tags.forEach(tag -> this.addTag(tag, namespace));
    }

    public void addTag(TreeTagDto tag, NamespaceDto namespace) {
        addTag(tag, namespace.root());
    }

    private static void addTag(TreeTagDto tag, TreeTagDto root) {
        if (!root.children().containsKey(tag.name())) {
            tag.reparent(root);
            return;
        }

        TreeTagDto newRoot = root.children().get(tag.name());
        Map.copyOf(tag.children()).forEach((name, child) -> addTag(child, newRoot));
    }

}

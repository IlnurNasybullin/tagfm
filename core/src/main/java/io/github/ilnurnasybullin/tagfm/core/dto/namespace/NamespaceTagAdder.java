package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.Collection;
import java.util.Map;

public class NamespaceTagAdder {

    private final NamespaceDto namespace;

    private NamespaceTagAdder(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static NamespaceTagAdder of(NamespaceDto namespace) {
        return new NamespaceTagAdder(namespace);
    }

    public void addTags(Collection<TreeTagDto> tags) {
        tags.forEach(this::addTag);
    }

    public void addTag(TreeTagDto tag) {
        addTag(namespace.root(), tag);
    }

    public static void addTag(TreeTagDto parent, TreeTagDto child) {
        String childName = child.name();
        Map<String, TreeTagDto> leafs = parent.children();
        if (!leafs.containsKey(childName)) {
            child.reparent(parent);
            return;
        }

        TreeTagDto newParent = leafs.get(childName);
        Map.copyOf(child.children()).forEach((name, newChild) -> addTag(newParent, newChild));
    }

}

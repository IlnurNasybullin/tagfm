package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagCollisionException;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.Map;

class UpChildrenWithoutConflicts implements TagRemover {

    private final NamespaceDto namespace;

    private UpChildrenWithoutConflicts(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static UpChildrenWithoutConflicts of(NamespaceDto namespace) {
        return new UpChildrenWithoutConflicts(namespace);
    }

    @Override
    public void removeTag(TreeTagDto tag) {
        TreeTagDto parent = tag.parent().orElseThrow();
        checkOnUnique(tag, parent);
        Map.copyOf(tag.children()).values()
                .forEach(child -> child.reparent(parent));

        tag.reparent(null);

        namespace.fileManager().removeTag(tag);
        namespace.synonymsManager().unbind(tag);
    }

    private void checkOnUnique(TreeTagDto tag, TreeTagDto parent) {
        Map<String, TreeTagDto> leafs = parent.children();
        tag.children()
                .keySet()
                .stream()
                .filter(leafs::containsKey)
                .findAny().ifPresent(name -> {
                    throw new TreeTagCollisionException(String.format(
                            "Child tag [%s] of removing tag [%s] already exist in parent tag [%s]!",
                            name, tag.name(), parent.name()
                    ));
                });
    }
}

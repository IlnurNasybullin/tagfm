package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.TagParentBindingStrategy;

public class TagParentBinding {

    private final NamespaceDto namespace;

    private TagParentBinding(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static TagParentBinding of(NamespaceDto namespace) {
        return new TagParentBinding(namespace);
    }

    public void bindParent(TreeTagDto tag, TreeTagDto parent, TagParentBindingStrategy strategy) {
        TagParentBinder binder = switch (strategy) {
            case THROW_IF_COLLISION -> new ThrowIfCollisionTagParentBinder();
            case REBASE_OLD -> RebaseOldTagParentBinder.of(namespace);
            case REBASE_NEW -> RebaseNewTagParentBinder.of(namespace);
            case MERGE -> MergeTagParentBinder.of(namespace);
        };
        binder.bindParent(tag, parent);
    }

}

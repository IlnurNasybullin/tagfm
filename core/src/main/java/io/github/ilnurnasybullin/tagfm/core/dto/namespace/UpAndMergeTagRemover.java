package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.CollisionWalker;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

class UpAndMergeTagRemover implements TagRemover {

    private final NamespaceDto namespace;

    private UpAndMergeTagRemover(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static UpAndMergeTagRemover of(NamespaceDto namespace) {
        return new UpAndMergeTagRemover(namespace);
    }

    @Override
    public void removeTag(TreeTagDto tag) {
        TreeTagDto parent = tag.parent().orElseThrow();

        CollisionWalker walker = CollisionWalker.of(this::hasCollision, this::noCollision);
        walker.walk(tag, parent);

        namespace.synonymsManager().unbind(tag);
        namespace.fileManager().removeTag(tag);
        tag.reparent(null);
    }

    private void noCollision(TreeTagDto primaryChild, TreeTagDto collisionParent) {
        primaryChild.reparent(collisionParent);
    }

    private void hasCollision(TreeTagDto primaryChild, TreeTagDto collisionChild) {
        namespace.fileManager().replaceTag(primaryChild, collisionChild);
        namespace.synonymsManager().replace(primaryChild, collisionChild);
        primaryChild.reparent(null);
    }
}

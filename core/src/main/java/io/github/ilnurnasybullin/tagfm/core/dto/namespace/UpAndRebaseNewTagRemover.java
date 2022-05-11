package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.CollisionWalker;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

class UpAndRebaseNewTagRemover implements TagRemover {

    private final NamespaceDto namespace;

    private UpAndRebaseNewTagRemover(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static UpAndRebaseNewTagRemover of(NamespaceDto namespace) {
        return new UpAndRebaseNewTagRemover(namespace);
    }

    @Override
    public void removeTag(TreeTagDto tag) {
        TreeTagDto parent = tag.parent().orElseThrow();

        CollisionWalker walker = CollisionWalker.of(this::hasCollision, this::noCollision);
        walker.walk(parent, tag);

        namespace.synonymsManager().unbind(tag);
        namespace.fileManager().removeTag(tag);
        tag.reparent(null);
    }

    private void noCollision(TreeTagDto primaryChild, TreeTagDto collisionParent) {
        primaryChild.reparent(collisionParent);
    }

    private void hasCollision(TreeTagDto primaryChild, TreeTagDto collisionChild) {
        namespace.fileManager().removeTag(primaryChild);
        namespace.synonymsManager().unbind(primaryChild);
        primaryChild.reparent(null);
    }

}

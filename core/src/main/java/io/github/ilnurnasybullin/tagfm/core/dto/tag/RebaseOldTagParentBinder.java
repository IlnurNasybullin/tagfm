package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;

import java.util.Map;

class RebaseOldTagParentBinder implements TagParentBinder {

    private final NamespaceDto namespace;

    private RebaseOldTagParentBinder(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static RebaseOldTagParentBinder of(NamespaceDto namespace) {
        return new RebaseOldTagParentBinder(namespace);
    }

    @Override
    public void bindParent(TreeTagDto tag, TreeTagDto parent) {
        Map<String, TreeTagDto> leafs = parent.children();
        String tagName = tag.name();
        if (!leafs.containsKey(tagName)) {
            tag.reparent(parent);
            return;
        }

        CollisionWalker walker = CollisionWalker.of(this::hasCollision, this::noCollision);
        walker.walk(tag, leafs.get(tagName));

        namespace.fileManager().removeTag(tag);
        namespace.synonymsManager().unbind(tag);
        tag.reparent(null);
    }

    private void noCollision(TreeTagDto primaryChild, TreeTagDto noCollisionParent) {
        primaryChild.reparent(noCollisionParent);
    }

    private void hasCollision(TreeTagDto primaryChildTag, TreeTagDto collisionChildTag) {
        namespace.synonymsManager().unbind(primaryChildTag);
        namespace.fileManager().removeTag(primaryChildTag);
        primaryChildTag.reparent(null);
    }
}

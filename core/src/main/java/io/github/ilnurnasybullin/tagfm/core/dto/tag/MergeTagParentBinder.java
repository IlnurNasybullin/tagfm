package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;

import java.util.Map;

class MergeTagParentBinder implements TagParentBinder {

    private final NamespaceDto namespace;

    private MergeTagParentBinder(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static MergeTagParentBinder of(NamespaceDto namespace) {
        return new MergeTagParentBinder(namespace);
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
        TreeTagDto collisionTag = leafs.get(tagName);
        walker.walk(tag, collisionTag);

        namespace.fileManager().replaceTag(tag, collisionTag);
        namespace.synonymsManager().replace(tag, collisionTag);
        tag.reparent(null);
    }

    private void noCollision(TreeTagDto primaryChild, TreeTagDto noCollisionParent) {
        primaryChild.reparent(noCollisionParent);
    }

    private void hasCollision(TreeTagDto primaryChildTag, TreeTagDto collisionChildTag) {
        namespace.synonymsManager().replace(primaryChildTag, collisionChildTag);
        namespace.fileManager().replaceTag(primaryChildTag, collisionChildTag);
        primaryChildTag.reparent(null);
    }
}

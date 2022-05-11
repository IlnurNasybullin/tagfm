package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;

import java.util.Map;

class RebaseNewTagParentBinder implements TagParentBinder {

    private final NamespaceDto namespace;

    private RebaseNewTagParentBinder(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static RebaseNewTagParentBinder of(NamespaceDto namespace) {
        return new RebaseNewTagParentBinder(namespace);
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
        TreeTagDto removingTag = leafs.get(tagName);
        walker.walk(removingTag, tag);

        namespace.synonymsManager().unbind(removingTag);
        namespace.fileManager().removeTag(removingTag);

        removingTag.reparent(null);
        tag.reparent(parent);
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

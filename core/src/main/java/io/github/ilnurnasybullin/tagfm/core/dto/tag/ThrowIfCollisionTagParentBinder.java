package io.github.ilnurnasybullin.tagfm.core.dto.tag;

class ThrowIfCollisionTagParentBinder implements TagParentBinder {

    @Override
    public void bindParent(TreeTagDto tag, TreeTagDto parent) {
        if (parent.children().containsKey(tag.name())) {
            throw new TreeTagCollisionException(
                    String.format("Parent tag [%s] already has child tag with name [%s]", tag.name(), parent.name())
            );
        }

        tag.reparent(parent);
    }
}

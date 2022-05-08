package io.github.ilnurnasybullin.tagfm.core.dto.tag;

public abstract class AbstractTreeTag implements TagDto {

    protected final static String SEPARATOR = "/";

    protected String calculateFullName(TreeTagDto parent, String name) {
        return fullName(parent, name);
    }

    protected static String fullName(TreeTagDto parent, String name) {
        return parent == null ? name : String.format("%s%s%s", parent.fullName(), SEPARATOR, name);
    }

}

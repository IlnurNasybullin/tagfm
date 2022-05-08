package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import io.github.ilnurnasybullin.tagfm.core.service.Publisher;

import java.util.HashMap;
import java.util.Map;

public class TreeTag extends TreeTagValidator {

    private static final Publisher publisher;
    public static final String PARENT_TAG_PROPERTY = "tag-parent";
    public static final String FULL_NAME_PROPERTY = "full-name-property";

    static {
        publisher = Publisher.get();

        publisher.addSubscriber(PARENT_TAG_PROPERTY, TreeTag::reparent);
        publisher.addSubscriber(FULL_NAME_PROPERTY, TreeTag::recalculateFullName);
    }

    protected TreeTag(String name, String fullName, TreeTagDto parent, Map<String, TreeTagDto> children) {
        super(name, fullName, parent, children);
    }

    private static TreeTagDto initWithoutParent(String name) {
        return initWithParent(name, null);
    }

    public static TreeTagDto initWithParent(String name, TreeTagDto parent) {
        return of(name, parent, new HashMap<>());
    }

    private static TreeTagDto of(String name, TreeTagDto parent, Map<String, TreeTagDto> children) {
        return new TreeTag(name, fullName(parent, name), parent, children);
    }

    public static TreeTagDto root() {
        return initWithoutParent("");
    }

    @Override
    protected TreeTagDto recalculateFullName() {
        TreeTagDto newTag = super.recalculateFullName();
        publisher.publish(FULL_NAME_PROPERTY, this, newTag);
        return newTag;
    }

    @Override
    public TreeTagDto reparent(TreeTagDto newParent) {
        TreeTagDto newValue = super.reparent(newParent);
        publisher.publish(PARENT_TAG_PROPERTY, this, newValue);
        publisher.publish(FULL_NAME_PROPERTY, this, newValue);
        return newValue;
    }

    private static void recalculateFullName(Object oldValue, Object newValue) {
        TreeTagDto newTag = (TreeTagDto) newValue;
        newTag.children().replaceAll((name, child) -> child.recalculateFullName());
    }

    private static void reparent(Object oldValue, Object newValue) {
        TreeTagDto oldTag = (TreeTagDto) oldValue;
        TreeTagDto newTag = (TreeTagDto) newValue;

        oldTag.parent().ifPresent(parent -> parent.children().remove(oldTag.name()));
        newTag.parent().ifPresent(parent -> parent.children().put(newTag.name(), newTag));
    }
}

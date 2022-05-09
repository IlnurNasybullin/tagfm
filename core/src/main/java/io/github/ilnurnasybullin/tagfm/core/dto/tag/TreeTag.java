package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TreeTag extends TreeTagValidator {

    protected TreeTag(String name, String fullName, TreeTagDto parent, Map<String, TreeTagDto> children) {
        super(name, fullName, parent, children);
        if (parent != null) {
            parent.children().put(name, this);
        }
    }

    private static TreeTagDto initWithoutParent(String name) {
        return initWithParent(name, null);
    }

    public static TreeTagDto initWithParent(String name, TreeTagDto parent) {
        return init(name, parent, new HashMap<>());
    }

    private static TreeTagDto init(String name, TreeTagDto parent, Map<String, TreeTagDto> children) {
        return new TreeTag(name, fullName(parent, name), parent, children);
    }

    public static TreeTagDto root() {
        return initWithoutParent("");
    }

    public static TreeTagDto of(String name) {
        TreeTagValidator.checkName(name);
        return initWithoutParent(name);
    }

    public static TreeTagDto of(String name, TreeTagDto parent) {
        TreeTagValidator.checkName(name);
        TreeTagValidator.checkOnUniqueLeaf(name, Optional.ofNullable(parent));
        return initWithParent(name, parent);
    }

    @Override
    public void reparent(TreeTagDto newParent) {
        Optional<TreeTagDto> oldParent = parent();
        super.reparent(newParent);
        oldParent.ifPresent(parent -> parent.children().remove(name()));
        if (newParent != null) {
            newParent.children().put(name(), this);
        }
    }
}

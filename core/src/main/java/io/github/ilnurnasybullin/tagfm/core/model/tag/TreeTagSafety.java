package io.github.ilnurnasybullin.tagfm.core.model.tag;

import io.github.ilnurnasybullin.tagfm.core.api.dto.InvalidTagNameException;
import io.github.ilnurnasybullin.tagfm.core.api.service.TreeTagCollisionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author Ilnur Nasybullin
 */
public final class TreeTagSafety extends TreeTag {

    private static final Predicate<String> illegalNamePatternPredicate =
            Pattern.compile("^\\s|\\s$|[/,]+").asPredicate();

    private TreeTagSafety(String name, String fullName, TreeTag parent, Map<String, TreeTag> children) {
        super(name, fullName, parent, children);
        if (parent != null) {
            parent.children().put(name, this);
        }
    }

    private static void checkName(String name) {
        if (name == null || name.isEmpty() || illegalNamePatternPredicate.test(name)) {
            throw new InvalidTagNameException(String.format("Value [%s] is invalid for tag name", name));
        }
    }

    private static void checkOnUniqueLeaf(String name, Optional<TreeTag> parent) {
        if (parent.stream()
                .map(TreeTag::children)
                .anyMatch(children -> children.containsKey(name))) {
            throw new TreeTagCollisionException(
                    String.format("Name [%s] isn't unique in parent tag [%s]", name,
                            parent.map(TreeTag::name)
                                    .orElse(null))
            );
        }
    }

    private static TreeTag initWithoutParent(String name) {
        return initWithParent(name, null);
    }

    public static TreeTag initWithParent(String name, TreeTag parent) {
        return init(name, parent, new HashMap<>());
    }

    private static TreeTag init(String name, TreeTag parent, Map<String, TreeTag> children) {
        return new TreeTagSafety(name, calculateFullName(parent, name), parent, children);
    }

    public static TreeTag root() {
        return initWithoutParent("");
    }

    public static TreeTag of(String name) {
        TreeTagSafety.checkName(name);
        return initWithoutParent(name);
    }

    public static TreeTag of(String name, TreeTag parent) {
        TreeTagSafety.checkName(name);
        TreeTagSafety.checkOnUniqueLeaf(name, Optional.ofNullable(parent));
        return initWithParent(name, parent);
    }

    private static String calculateFullName(TreeTag parent, String name) {
        return parent == null ? name : String.format("%s%s%s", parent.fullName(), SEPARATOR, name);
    }

    private void recalculateFullName() {
        renameFullName(calculateFullName(parent().orElse(null), name()));
    }

    private void checkOnUniqueLeaf(String name) {
        checkOnUniqueLeaf(name, parent());
    }

    @Override
    public void rename(String newName) {
        checkName(newName);
        checkOnUniqueLeaf(newName);
        super.rename(newName);
        recalculateFullName();
    }

    @Override
    public void reparent(TreeTag newParent) {
        Optional<TreeTag> oldParent = parent();
        super.reparent(newParent);
        oldParent.ifPresent(parent -> parent.children().remove(name()));
        if (newParent != null) {
            newParent.children().put(name(), this);
        }
        recalculateFullName();
    }
    
}

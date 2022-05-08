package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class TreeTagValidator extends TreeTagDto {

    protected static final Predicate<String> illegalNamePatternPredicate =
            Pattern.compile("^\\s|\\s$|/").asPredicate();

    protected TreeTagValidator(String name, String fullName, TreeTagDto parent, Map<String, TreeTagDto> children) {
        super(name, fullName, parent, children);
    }

    private void checkName(String name) {
        if (name == null || illegalNamePatternPredicate.test(name)) {
            throw new InvalidTagNameException(String.format("Value [%s] is invalid for tag name", name));
        }
    }

    private void checkOnUniqueLeaf(String name) {
        if (parent().stream()
                .map(TreeTagDto::children)
                .anyMatch(children -> children.containsKey(name))) {
            throw new UniqueLeafNameConstraintException(
                    String.format("Name [%s] isn't unique in parent tag [%s]", name, parent()
                            .map(TreeTagDto::name)
                            .orElse(null))
            );
        }
    }

    @Override
    public TreeTagDto rename(String newName) {
        checkName(newName);
        checkOnUniqueLeaf(newName);
        return super.rename(newName);
    }
}

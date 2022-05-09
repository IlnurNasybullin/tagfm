package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.Optional;
import java.util.regex.Pattern;

public class TreeTagCreator {

    private final static Pattern splitter = Pattern.compile("/");

    public Optional<TreeTagDto> deepCreate(String fullName) {
        String[] names = splitter.split(fullName);
        if (names.length == 0) {
            return Optional.empty();
        }

        int startIndex = names[0].isEmpty() ? 1 : 0;
        TreeTagDto root = TreeTag.root();
        try {
            TreeTagDto tag = root;
            for (int i = startIndex; i < names.length; i++) {
                tag = TreeTag.of(names[i], tag);
            }
        } catch (InvalidTagNameException e) {
            throw  new InvalidTagNameException(
                    String.format("Invalid creating tag with full fullName [%s]", fullName), e
            );
        }

        Optional<TreeTagDto> tagRoot = root.children()
                .values()
                .stream()
                .findAny();
        tagRoot.ifPresent(t -> t.reparent(null));

        return tagRoot;
    }
}

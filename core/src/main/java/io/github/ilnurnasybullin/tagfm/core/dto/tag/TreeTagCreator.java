package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.Optional;

public class TreeTagCreator {

    public Optional<TreeTagDto> deepCreate(String fullName) {
        String[] names = new TreeTagSplitter().tagNames(fullName);
        TreeTagDto root = TreeTag.root();
        try {
            TreeTagDto tag = root;
            for (String name: names) {
                tag = TreeTag.of(name, tag);
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

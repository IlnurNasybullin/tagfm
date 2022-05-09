package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagSplitter;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class TreeTagSearcher {

    public Optional<TreeTagDto> findByFullName(String fullName, NamespaceDto namespace) {
        String[] tagNames = new TreeTagSplitter().tagNames(fullName);
        TreeTagDto tag = namespace.root();

        int i = 0;
        while (i < tagNames.length && tag != null) {
            tag = tag.children().get(tagNames[i]);
            i++;
        }

        return Optional.ofNullable(tag);
    }

    public Stream<TreeTagDto> findByName(String name, NamespaceDto namespace) {
        return namespace.tags()
                .filter(tag -> Objects.equals(tag.name(), name));
    }

}

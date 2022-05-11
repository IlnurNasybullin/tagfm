package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagSplitter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NamespaceTagSearcher {

    private final NamespaceDto namespace;

    private NamespaceTagSearcher(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    public static NamespaceTagSearcher of(NamespaceDto namespace) {
        return new NamespaceTagSearcher(namespace);
    }

    public Optional<TreeTagDto> findByFullName(String fullName) {
        String[] tagNames = new TreeTagSplitter().tagNames(fullName);
        TreeTagDto tag = namespace.root();

        int i = 0;
        while (i < tagNames.length && tag != null) {
            tag = tag.children().get(tagNames[i]);
            i++;
        }

        return Optional.ofNullable(tag);
    }

    public Stream<TreeTagDto> findByName(String name) {
        return namespace.tags()
                .filter(tag -> Objects.equals(tag.name(), name));
    }

    public Map<String, List<TreeTagDto>> findByNames(Collection<String> names) {
        Set<String> setNames = Set.copyOf(names);
        return namespace.tags()
                .filter(tag -> setNames.contains(tag.name()))
                .collect(Collectors.groupingBy(TreeTagDto::name));
    }

}

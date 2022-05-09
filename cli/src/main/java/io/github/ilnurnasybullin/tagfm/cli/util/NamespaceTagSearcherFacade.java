package io.github.ilnurnasybullin.tagfm.cli.util;

import io.github.ilnurnasybullin.tagfm.cli.command.MultiplyTagSelectionException;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceTagSearcher;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

public class NamespaceTagSearcherFacade {

    public TreeTagDto searchTag(String name, NamespaceDto namespace, boolean byShortName) {
        NamespaceTagSearcher tagSearcher = new NamespaceTagSearcher();

        if (!byShortName) {
            return tagSearcher.findByFullName(name, namespace).orElseThrow(() ->
                    tagNotFound(name, namespace)
            );
        }

        TreeTagDto[] tags = tagSearcher.findByName(name, namespace).toArray(TreeTagDto[]::new);
        if (tags.length == 0) {
            throw tagNotFound(name, namespace);
        }
        if (tags.length > 1) {
            throw new MultiplyTagSelectionException(String.format(
                    "Tag with short name [%s] is not unique in namespace [%s]!",
                    name, namespace.name()
            ));
        }

        return tags[0];
    }

    private IllegalArgumentException tagNotFound(String name, NamespaceDto namespace) {
        return new IllegalArgumentException(String.format(
                "Tag with name [%s] is not found in namespace [%s]!",
                name, namespace.name()
        ));
    }

}

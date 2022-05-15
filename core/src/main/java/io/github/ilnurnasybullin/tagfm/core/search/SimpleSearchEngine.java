package io.github.ilnurnasybullin.tagfm.core.search;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.namespace.NamespaceDto;

/**
 * @author Ilnur Nasybullin
 */
public class SimpleSearchEngine implements TaggedFilesSearchEngine {

    private final NamespaceDto namespace;

    public SimpleSearchEngine(NamespaceDto namespace) {
        this.namespace = namespace;
    }

    @Override
    public boolean test(TaggedFileDto taggedFile) {
        return false;
    }
}

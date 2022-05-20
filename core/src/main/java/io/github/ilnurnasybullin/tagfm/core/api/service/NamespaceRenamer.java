package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.NamespaceRenamingService;
import io.github.ilnurnasybullin.tagfm.core.model.namespace.Namespace;

/**
 * @author Ilnur Nasybullin
 */
public class NamespaceRenamer implements NamespaceRenamingService {

    private final Namespace namespace;

    private NamespaceRenamer(Namespace namespace) {
        this.namespace = namespace;
    }

    public static NamespaceRenamer of(io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace namespace) {
        return new NamespaceRenamer((Namespace) namespace);
    }

    @Override
    public void rename(String newName) {
        namespace.rename(newName);
    }
}

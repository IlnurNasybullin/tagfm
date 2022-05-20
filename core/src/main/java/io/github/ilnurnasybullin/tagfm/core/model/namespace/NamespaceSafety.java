package io.github.ilnurnasybullin.tagfm.core.model.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.model.file.TaggedFile;
import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTagSafety;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public final class NamespaceSafety extends Namespace {

    private NamespaceSafety(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTag root,
                            SynonymTagManager synonymsManager, Set<TaggedFile> files) {
        super(name, created, fileNaming, root, synonymsManager, files);
    }

    public static Namespace init(String name, FileNamingStrategy fileNaming) {
        ZonedDateTime now = LocalDateTime.now(Clock.systemUTC()).atZone(ZoneId.of("UTC"));
        Set<TaggedFile> files = Collections.newSetFromMap(new IdentityHashMap<>());
        return of(name, now, fileNaming, TreeTagSafety.root(), SynonymTagManager.init(), files);
    }

    public static Namespace of(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTag root,
                               SynonymTagManager synonymsManager, Set<TaggedFile> files) {
        checkName(name);
        return new NamespaceSafety(name, created, fileNaming, root, synonymsManager, files);
    }

    @Override
    public void rename(String newName) {
        checkName(newName);
        super.rename(newName);
    }

    private static void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNamespaceNameException(String.format("Invalid name [%s] for namespace", name));
        }
    }
}

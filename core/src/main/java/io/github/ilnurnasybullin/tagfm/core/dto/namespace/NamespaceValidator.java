package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileManager;
import io.github.ilnurnasybullin.tagfm.core.dto.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.time.ZonedDateTime;

public class NamespaceValidator extends NamespaceDto {
    protected NamespaceValidator(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                                 SynonymTagManager synonymsManager, TaggedFileManager fileManager) {
        super(name, created, fileNaming, ROOT, synonymsManager, fileManager);
    }

    @Override
    public void rename(String newName) {
        checkName(newName);
        super.rename(newName);
    }

    private void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNamespaceNameException(String.format("Invalid name [%s] for namespace", name));
        }
    }
}

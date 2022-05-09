package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class NamespaceValidator extends NamespaceDto {
    protected NamespaceValidator(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                                 List<Set<TreeTagDto>> synonyms, Set<TaggedFileDto> files) {
        super(name, created, fileNaming, ROOT, synonyms, files);
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

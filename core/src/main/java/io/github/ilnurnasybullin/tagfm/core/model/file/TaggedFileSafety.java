package io.github.ilnurnasybullin.tagfm.core.model.file;

import io.github.ilnurnasybullin.tagfm.core.api.dto.UnexistingFileException;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * @author Ilnur Nasybullin
 */
public final class TaggedFileSafety extends TaggedFile {

    private TaggedFileSafety(Path file, Set<TreeTag> tags) {
        super(file, tags);
    }

    public static TaggedFile init(Path file) {
        return initWithTags(file, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    public static TaggedFile initWithTags(Path file, Set<TreeTag> tags) {
        return new TaggedFileSafety(file, tags);
    }

    @Override
    public void replace(Path file) {
        checkFile(file);
        super.replace(file);
    }

    private void checkFile(Path file) {
        if (Files.notExists(file)) {
            throw new UnexistingFileException(
                    String.format("File or dir [%s] isn't existing!", file)
            );
        }
    }

}

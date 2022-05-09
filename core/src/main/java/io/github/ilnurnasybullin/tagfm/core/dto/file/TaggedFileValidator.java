package io.github.ilnurnasybullin.tagfm.core.dto.file;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class TaggedFileValidator extends TaggedFileDto {
    protected TaggedFileValidator(Path file, Set<TreeTagDto> tags) {
        super(file, tags);
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

package io.github.ilnurnasybullin.tagfm.core.dto.file;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.service.Publisher;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.BiConsumer;

public class TaggedFile extends TaggedFileValidator {

    private static final Publisher treeTagPublisher;
    public static final String FILE_PROPERTY = "file";

    private final BiConsumer<Object, Object> updateTag;

    static {
        treeTagPublisher = Publisher.get();
    }

    protected TaggedFile(Path file, Set<TreeTagDto> tags) {
        super(file, tags);

        updateTag = this::updateTag;
        treeTagPublisher.addSubscriber(TreeTag.PARENT_TAG_PROPERTY, updateTag);
        treeTagPublisher.addSubscriber(TreeTag.FULL_NAME_PROPERTY, updateTag);
    }

    public static TaggedFileDto initWithTags(Path file, Set<TreeTagDto> tags) {
        return new TaggedFile(file, tags);
    }

    @Override
    public TaggedFileDto replace(Path file) {
        TaggedFileDto newTaggedFile = super.replace(file);
        treeTagPublisher.removeSubscriber(TreeTag.PARENT_TAG_PROPERTY, updateTag);
        treeTagPublisher.removeSubscriber(TreeTag.FULL_NAME_PROPERTY, updateTag);
        return newTaggedFile;
    }

    private void updateTag(Object oldValue, Object newValue) {
        TreeTagDto oldTag = (TreeTagDto) oldValue;
        TreeTagDto newTag = (TreeTagDto) newValue;

        if (tags().remove(oldTag)) {
            tags().add(newTag);
        }
    }
}

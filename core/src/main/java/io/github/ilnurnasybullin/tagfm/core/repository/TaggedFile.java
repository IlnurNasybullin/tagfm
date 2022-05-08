package io.github.ilnurnasybullin.tagfm.core.repository;

import java.nio.file.Path;
import java.util.Set;

public interface TaggedFile {
    <T> Set<T> tags();
    Path file();
}

package io.github.ilnurnasybullin.tagfm.api.service;

import java.nio.file.Path;

public interface TaggedFileService<TF> {
    TF copy(TF taggedFile, Path outerFile);
    TF replace(TF taggedFile, Path outerFile);
}

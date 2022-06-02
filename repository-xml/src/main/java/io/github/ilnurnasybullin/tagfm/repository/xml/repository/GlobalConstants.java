package io.github.ilnurnasybullin.tagfm.repository.xml.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public final class GlobalConstants {

    public final static Path ROOT_DIR = Paths.get("./.tagfm");
    public final static Path CONFIG_FILE = ROOT_DIR.resolve("config");
    public final static String NAMESPACE_FILE_EXTENSION = ".xml";
    public final static Function<String, Path> NAMESPACE_RESOLVER =
            name -> ROOT_DIR.resolve(name + NAMESPACE_FILE_EXTENSION);

    private GlobalConstants() {
        throw new IllegalStateException();
    }

}

module tagged.file.manager.cli {
    requires info.picocli;
    requires tagged.file.manager.core;
    requires tagged.file.manager.api;

    requires io.micronaut.core;
    requires io.micronaut.inject;

    requires jakarta.inject;
    requires jakarta.annotation;

    exports io.github.ilnurnasybullin.tagfm.cli.beans to io.micronaut.core;
    exports io.github.ilnurnasybullin.tagfm.cli.command to io.micronaut.core;

    opens io.github.ilnurnasybullin.tagfm.cli.command;
}
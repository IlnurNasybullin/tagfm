module tagfm.cli {
    requires info.picocli;
    requires tagfm.core;
    requires tagfm.repository.xml;

    requires io.micronaut.core;
    requires io.micronaut.inject;

    requires jakarta.inject;
    requires jakarta.annotation;

    exports io.github.ilnurnasybullin.tagfm.cli.beans;
    exports io.github.ilnurnasybullin.tagfm.cli.command;
    exports io.github.ilnurnasybullin.tagfm.cli.format;

    opens io.github.ilnurnasybullin.tagfm.cli.command;
}
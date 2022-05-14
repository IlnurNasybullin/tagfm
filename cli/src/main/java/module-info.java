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
    opens io.github.ilnurnasybullin.tagfm.cli.format;
    exports io.github.ilnurnasybullin.tagfm.cli.command.addFiles;
    opens io.github.ilnurnasybullin.tagfm.cli.command.addFiles;
    exports io.github.ilnurnasybullin.tagfm.cli.command.bind;
    opens io.github.ilnurnasybullin.tagfm.cli.command.bind;
    exports io.github.ilnurnasybullin.tagfm.cli.command.list;
    opens io.github.ilnurnasybullin.tagfm.cli.command.list;
    exports io.github.ilnurnasybullin.tagfm.cli.command.namespace;
    opens io.github.ilnurnasybullin.tagfm.cli.command.namespace;
    exports io.github.ilnurnasybullin.tagfm.cli.command.print;
    opens io.github.ilnurnasybullin.tagfm.cli.command.print;
    exports io.github.ilnurnasybullin.tagfm.cli.command.removeTag;
    opens io.github.ilnurnasybullin.tagfm.cli.command.removeTag;
    exports io.github.ilnurnasybullin.tagfm.cli.command.renameTag;
    opens io.github.ilnurnasybullin.tagfm.cli.command.renameTag;
    exports io.github.ilnurnasybullin.tagfm.cli.command.unbind;
    opens io.github.ilnurnasybullin.tagfm.cli.command.unbind;
}
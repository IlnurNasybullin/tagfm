/*
 * Copyright 2022 Ilnur Nasybullin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

module tagfm.cli {
    requires info.picocli;

    requires tagfm.core;
    requires tagfm.repository.xml;
    requires tagfm.logical.expression.spi.provider;

    requires io.micronaut.core;
    requires io.micronaut.inject;

    requires jakarta.inject;
    requires jakarta.annotation;

    exports io.github.ilnurnasybullin.tagfm.cli.beans;
    exports io.github.ilnurnasybullin.tagfm.cli.command;
    exports io.github.ilnurnasybullin.tagfm.cli.format;

    opens io.github.ilnurnasybullin.tagfm.cli.command;
    opens io.github.ilnurnasybullin.tagfm.cli.format;
    exports io.github.ilnurnasybullin.tagfm.cli.command.tag.bind;
    opens io.github.ilnurnasybullin.tagfm.cli.command.tag.bind;
    exports io.github.ilnurnasybullin.tagfm.cli.command.print.list;
    opens io.github.ilnurnasybullin.tagfm.cli.command.print.list;
    exports io.github.ilnurnasybullin.tagfm.cli.command.namespace;
    opens io.github.ilnurnasybullin.tagfm.cli.command.namespace;
    exports io.github.ilnurnasybullin.tagfm.cli.command.print;
    opens io.github.ilnurnasybullin.tagfm.cli.command.print;
    exports io.github.ilnurnasybullin.tagfm.cli.command.tag.unbind;
    opens io.github.ilnurnasybullin.tagfm.cli.command.tag.unbind;

    exports io.github.ilnurnasybullin.tagfm.cli.command.file.merge;
    opens io.github.ilnurnasybullin.tagfm.cli.command.file.merge;

    exports io.github.ilnurnasybullin.tagfm.cli.util;
    opens io.github.ilnurnasybullin.tagfm.cli.util;
    exports io.github.ilnurnasybullin.tagfm.cli.command.tag;
    opens io.github.ilnurnasybullin.tagfm.cli.command.tag;
    exports io.github.ilnurnasybullin.tagfm.cli.command.file;
    opens io.github.ilnurnasybullin.tagfm.cli.command.file;
    exports io.github.ilnurnasybullin.tagfm.cli.command.file.unbind;
    opens io.github.ilnurnasybullin.tagfm.cli.command.file.unbind;

    exports io.github.ilnurnasybullin.tagfm.cli.command.option;
    opens io.github.ilnurnasybullin.tagfm.cli.command.option;
}
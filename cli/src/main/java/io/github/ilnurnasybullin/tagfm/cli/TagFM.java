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

package io.github.ilnurnasybullin.tagfm.cli;

import io.github.ilnurnasybullin.tagfm.cli.command.FileManagerCommand;
import io.github.ilnurnasybullin.tagfm.cli.handler.PrintExceptionMessageHandler;
import io.github.ilnurnasybullin.tagfm.cli.util.CustomFactory;
import io.micronaut.context.ApplicationContext;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

public class TagFM {

    public static void main(String[] args) {
        try(ApplicationContext context = ApplicationContext.run(args);
            FileManagerCommand command = context.getBean(FileManagerCommand.class)) {
            IFactory cfFactory = CommandLine.defaultFactory();
            CustomFactory factory = new CustomFactory(context, cfFactory);
            CommandLine commandLine = new CommandLine(command, factory)
                    .setExecutionExceptionHandler(new PrintExceptionMessageHandler())
                    .setCaseInsensitiveEnumValuesAllowed(true);

            commandLine.execute(args);
        }
    }

}

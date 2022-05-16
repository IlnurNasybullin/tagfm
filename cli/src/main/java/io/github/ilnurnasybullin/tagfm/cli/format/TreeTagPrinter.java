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

package io.github.ilnurnasybullin.tagfm.cli.format;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.util.*;

/**
 * @author Ilnur Nasybullin
 */
public class TreeTagPrinter {

    private enum PrintState {
        PRINT_TAG_NAME,
        PRINT_BORDER_WITH_SEQUEL,
        PRINT_BORDER_WITHOUT_SEQUEL,
        PRINT_SEQUEL,
        PRINT_EMPTY_STRING
    }

    private static class TagPrinterStateMachine {

        private final TreeTagDto root;

        private final static String BORDER_WITH_SEQUEL = "\u251c\u2500\u2500\u2500";
        private final static String BORDER_WITHOUT_SEQUEL = "\u2514\u2500\u2500\u2500";
        private final static String SEQUEL = "\u2502   ";
        private final static String EMPTY_STRING = "    ";

        private PrintState currentState;

        private final TreeMap<String, TreeTagDto> children;

        private TagPrinterStateMachine(TreeTagDto root) {
            this.root = root;
            this.children = new TreeMap<>(root.children());
            this.currentState = PrintState.PRINT_TAG_NAME;
        }

        public Optional<TreeTagDto> next() {
            if (children.isEmpty()) {
                return Optional.empty();
            }

            Map.Entry<String, TreeTagDto> entry = children.firstEntry();
            children.remove(entry.getKey());

            currentState = children.isEmpty() ?
                    PrintState.PRINT_BORDER_WITHOUT_SEQUEL :
                    PrintState.PRINT_BORDER_WITH_SEQUEL;

            return Optional.of(entry.getValue());
        }

        public void print() {
            PrintState nextState = currentState;
            String expression = switch (currentState) {
                case PRINT_TAG_NAME -> root.name();
                case PRINT_SEQUEL -> SEQUEL;
                case PRINT_BORDER_WITH_SEQUEL -> {
                    nextState = PrintState.PRINT_SEQUEL;
                    yield BORDER_WITH_SEQUEL;
                }
                case PRINT_BORDER_WITHOUT_SEQUEL -> {
                    nextState = PrintState.PRINT_EMPTY_STRING;
                    yield BORDER_WITHOUT_SEQUEL;
                }
                case PRINT_EMPTY_STRING -> EMPTY_STRING;
            };
            System.out.print(expression);

            currentState = nextState;
        }

    }

    private final TreeTagDto root;

    public TreeTagPrinter(TreeTagDto root) {
        this.root = root;
    }

    public void print() {
        Deque<TagPrinterStateMachine> printers = new ArrayDeque<>();
        printers.add(new TagPrinterStateMachine(root));

        while (!printers.isEmpty()) {
            printers.forEach(TagPrinterStateMachine::print);

            TreeTagDto newRoot = null;
            while (!printers.isEmpty() && newRoot == null) {
                newRoot = printers.peekLast().next().orElse(null);
                if (newRoot == null) {
                    printers.removeLast();
                }
            }
            if (newRoot != null) {
                printers.addLast(new TagPrinterStateMachine(newRoot));
            }
            System.out.println();
        }

    }

}

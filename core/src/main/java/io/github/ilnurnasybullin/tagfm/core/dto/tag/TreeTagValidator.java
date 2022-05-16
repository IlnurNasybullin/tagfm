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

package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class TreeTagValidator extends TreeTagDto {

    protected static final Predicate<String> illegalNamePatternPredicate =
            Pattern.compile("^\\s|\\s$|[/,]+").asPredicate();

    protected TreeTagValidator(String name, String fullName, TreeTagDto parent, Map<String, TreeTagDto> children) {
        super(name, fullName, parent, children);
    }

    protected static void checkName(String name) {
        if (name == null || name.isEmpty() || illegalNamePatternPredicate.test(name)) {
            throw new InvalidTagNameException(String.format("Value [%s] is invalid for tag name", name));
        }
    }

    protected static void checkOnUniqueLeaf(String name, Optional<TreeTagDto> parent) {
        if (parent.stream()
                .map(TreeTagDto::children)
                .anyMatch(children -> children.containsKey(name))) {
            throw new TreeTagCollisionException(
                    String.format("Name [%s] isn't unique in parent tag [%s]", name,
                            parent.map(TreeTagDto::name)
                            .orElse(null))
            );
        }
    }

    private void checkOnUniqueLeaf(String name) {
        checkOnUniqueLeaf(name, parent());
    }

    @Override
    public void rename(String newName) {
        checkName(newName);
        checkOnUniqueLeaf(newName);
        super.rename(newName);
    }
}

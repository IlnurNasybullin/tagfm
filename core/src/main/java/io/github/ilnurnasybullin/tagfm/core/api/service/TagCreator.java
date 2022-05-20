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

package io.github.ilnurnasybullin.tagfm.core.api.service;

import io.github.ilnurnasybullin.tagfm.api.service.TagCreatorService;
import io.github.ilnurnasybullin.tagfm.core.api.dto.InvalidTagNameException;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.service.util.TreeTagSplitter;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTagSafety;

import java.util.Optional;

public class TagCreator implements TagCreatorService<Tag> {

    @Override
    public Optional<Tag> deepCreate(String fullName) {
        String[] names = new TreeTagSplitter().tagNames(fullName);
        TreeTag root = TreeTagSafety.root();
        try {
            TreeTag tag = root;
            for (String name: names) {
                tag = TreeTagSafety.of(name, tag);
            }
        } catch (InvalidTagNameException e) {
            throw  new InvalidTagNameException(
                    String.format("Invalid creating tag with full fullName [%s]", fullName), e
            );
        }

        Optional<TreeTag> tagRoot = root.children()
                .values()
                .stream()
                .findAny();
        tagRoot.ifPresent(t -> t.reparent(null));

        return tagRoot.map(t -> t);
    }
}

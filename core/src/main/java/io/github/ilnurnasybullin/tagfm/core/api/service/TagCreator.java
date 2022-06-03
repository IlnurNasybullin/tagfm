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
import io.github.ilnurnasybullin.tagfm.core.api.dto.TagView;
import io.github.ilnurnasybullin.tagfm.core.api.service.util.TreeTagSplitter;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTagSafety;

import java.util.Optional;

public class TagCreator implements TagCreatorService<TagView> {

    @Override
    public TagView createByFullName(String fullName) {
        String[] names = new TreeTagSplitter().tagNames(fullName);
        TreeTag root = TreeTagSafety.root();
        TreeTag creatingTag = root;
        try {
            for (String name: names) {
                creatingTag = TreeTagSafety.of(name, creatingTag);
            }
        } catch (InvalidTagNameException e) {
            throw  new InvalidTagNameException(
                    String.format("Invalid creating tag with full fullName [%s]", fullName), e
            );
        }

        root.children()
                .values()
                .stream()
                .findAny()
                .ifPresent(tag -> tag.reparent(null));

        return creatingTag;
    }
}

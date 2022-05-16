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

package io.github.ilnurnasybullin.tagfm.core.dto.file;

import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.nio.file.Path;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class TaggedFile extends TaggedFileValidator {

    protected TaggedFile(Path file, Set<TreeTagDto> tags) {
        super(file, tags);
    }

    public static TaggedFileDto init(Path file) {
        return initWithTags(file, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    public static TaggedFileDto initWithTags(Path file, Set<TreeTagDto> tags) {
        return new TaggedFile(file, tags);
    }
}

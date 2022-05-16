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

package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.api.service.FileNamingStrategy;
import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileManager;
import io.github.ilnurnasybullin.tagfm.core.dto.synonym.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;

import java.time.ZonedDateTime;

public class NamespaceValidator extends NamespaceDto {
    protected NamespaceValidator(String name, ZonedDateTime created, FileNamingStrategy fileNaming, TreeTagDto ROOT,
                                 SynonymTagManager synonymsManager, TaggedFileManager fileManager) {
        super(name, created, fileNaming, ROOT, synonymsManager, fileManager);
    }

    @Override
    public void rename(String newName) {
        checkName(newName);
        super.rename(newName);
    }

    private void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNamespaceNameException(String.format("Invalid name [%s] for namespace", name));
        }
    }
}

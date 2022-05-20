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

package io.github.ilnurnasybullin.tagfm.core.api.service.tagRemover;

import io.github.ilnurnasybullin.tagfm.core.api.dto.Namespace;
import io.github.ilnurnasybullin.tagfm.core.api.dto.SynonymTagManager;
import io.github.ilnurnasybullin.tagfm.core.api.dto.Tag;
import io.github.ilnurnasybullin.tagfm.core.api.service.FilesTagManager;
import io.github.ilnurnasybullin.tagfm.core.model.tag.TreeTag;
import io.github.ilnurnasybullin.tagfm.core.util.iterator.TreeIteratorsFactory;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class RemoveChildrenTagRemover implements TagRemover {

    private final Namespace namespace;

    public RemoveChildrenTagRemover(Namespace namespace) {
        this.namespace = namespace;
    }

    public static RemoveChildrenTagRemover of(Namespace namespace) {
        return new RemoveChildrenTagRemover(namespace);
    }

    @Override
    public void removeTag(TreeTag tag) {
        tag.reparent(null);

        Set<Tag> tags = Collections.newSetFromMap(new IdentityHashMap<>());
        TreeIteratorsFactory.HORIZONTAL_TRAVERSAL.SIMPLE.iterator(tag, t -> t.children().values())
                .forEachRemaining(tags::add);

        SynonymTagManager synonymManager = namespace.synonymsManager();
        tags.forEach(synonymManager::unbind);
        FilesTagManager.of(namespace).removeTags(tags);
    }
}

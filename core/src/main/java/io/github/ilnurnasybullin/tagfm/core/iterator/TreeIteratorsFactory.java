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

package io.github.ilnurnasybullin.tagfm.core.iterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Ilnur Nasybullin
 */
public enum TreeIteratorsFactory {;

    public enum HORIZONTAL_TRAVERSAL {;

        public enum SIMPLE {;

            public static <T> Iterator<T> iterator(T root, Function<T, Collection<T>> leafsSupplier) {
                return TreeIterator.horizontalTraversal(root, leafsSupplier);
            }

        }

        public enum LEVELED {;

            public static <T> Iterator<T> iterator(T root, Function<T, Collection<T>> leafsSupplier, int maxDepth) {
                return LevelTreeIterator.horizontalTraversal(root, leafsSupplier, maxDepth);
            }

        }

    }

}

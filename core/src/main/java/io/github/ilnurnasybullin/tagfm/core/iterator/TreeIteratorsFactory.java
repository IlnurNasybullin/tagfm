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

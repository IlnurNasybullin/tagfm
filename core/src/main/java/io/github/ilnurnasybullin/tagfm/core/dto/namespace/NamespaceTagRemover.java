package io.github.ilnurnasybullin.tagfm.core.dto.namespace;

import io.github.ilnurnasybullin.tagfm.core.dto.file.TaggedFileDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.TreeTagDto;
import io.github.ilnurnasybullin.tagfm.core.dto.tag.UniqueLeafNameConstraintException;
import io.github.ilnurnasybullin.tagfm.core.repository.TreeIterator;

import java.util.*;
import java.util.stream.Collectors;

public class NamespaceTagRemover {

    public void removeTag(TreeTagDto tag, NamespaceDto namespace, TagRemovingStrategy strategy) {
        TagRemover remover = instanceRemover(strategy);
        remover.removeTag(tag, namespace);
    }

    private TagRemover instanceRemover(TagRemovingStrategy strategy) {
        return switch (strategy) {
            case UP_CHILDREN_WITHOUT_CONFLICTS -> new UpChildrenWithoutConflicts();
            case REMOVE_CHILDREN -> new RemoveChildren();
            case UP_AND_REBASE_NEW -> new UpAndRebase(false);
            case UP_AND_REBASE_OLD -> new UpAndRebase(true);
            case UP_AND_MERGE_CHILDREN -> new UpAndMerge();
        };
    }

    interface TagRemover {
        void removeTag(TreeTagDto tag, NamespaceDto namespace);
        default void detachTag(TreeTagDto tag, NamespaceDto namespace) {
            namespace.synonyms()
                    .removeIf(tags -> tags.remove(tag) && tags.size() < 2);

            namespace.files()
                    .stream()
                    .map(TaggedFileDto::tags)
                    .forEach(tags -> tags.remove(tag));
        }
    }

    private static class UpChildrenWithoutConflicts implements TagRemover {

        @Override
        public void removeTag(TreeTagDto tag, NamespaceDto namespace) {
            tag.parent().ifPresent(parent ->  {
                checkOnUnique(tag, parent);
                Map.copyOf(tag.children()).values()
                        .forEach(child -> child.reparent(parent));
            });
            tag.reparent(null);
            detachTag(tag, namespace);
        }

        private void checkOnUnique(TreeTagDto tag, TreeTagDto parent) {
            Map<String, TreeTagDto> leafs = parent.children();
            tag.children()
                    .keySet()
                    .stream()
                    .filter(leafs::containsKey)
                    .findAny().ifPresent(name -> {
                        throw new UniqueLeafNameConstraintException(String.format(
                                "Child tag [%s] of removing tag [%s] already exist in parent tag [%s]!",
                                name, tag.name(), parent.name()
                        ));
                    });
        }
    }

    private static class RemoveChildren implements TagRemover {
        @Override
        public void removeTag(TreeTagDto tag, NamespaceDto namespace) {
            tag.reparent(null);
            detachTag(tag, namespace);
        }

        @Override
        public void detachTag(TreeTagDto tag, NamespaceDto namespace) {
            Set<TreeTagDto> tags = new HashSet<>();
            TreeIterator.horizontalTraversal(tag, t -> t.children().values())
                    .forEachRemaining(tags::add);

            namespace.synonyms()
                    .removeIf(synonyms -> synonyms.removeAll(tags) && synonyms.size() < 2);

            namespace.files()
                    .stream()
                    .map(TaggedFileDto::tags)
                    .forEach(fileTags -> tags.forEach(fileTags::remove));
        }
    }

    private abstract static class UpAndSomething implements TagRemover {

        private final boolean saveOld;

        protected UpAndSomething(boolean saveOld) {
            this.saveOld = saveOld;
        }

        @Override
        public void removeTag(TreeTagDto tag, NamespaceDto namespace) {
            TreeTagDto parent = tag.parent().orElse(null);
            if (parent == null) {
                return;
            }

            Map<TreeTagDto, TreeTagDto> replaceMap = new IdentityHashMap<>();
            tag.reparent(null);
            removeTag(tag, parent, replaceMap);

            tagsHandler(replaceMap, namespace);
        }

        protected abstract void tagsHandler(Map<TreeTagDto, TreeTagDto> replaceMap, NamespaceDto namespace);

        private void removeTag(TreeTagDto tag, TreeTagDto root, Map<TreeTagDto, TreeTagDto> replaceMap) {
            Map<String, TreeTagDto> leafs = root.children();
            Map<Boolean, List<TreeTagDto>> childrenConflicts = tag.children()
                    .entrySet()
                    .stream()
                    .collect(Collectors.partitioningBy(entry -> leafs.containsKey(entry.getKey()),
                            Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

            childrenConflicts.get(false).forEach(child -> child.reparent(root));
            childrenConflicts.get(true).forEach(child -> {
                TreeTagDto leafChild = leafs.get(child.name());

                TreeTagDto oldValue;
                TreeTagDto newValue;
                if (saveOld) {
                    oldValue = child;
                    newValue = leafChild;
                } else {
                    newValue = child;
                    oldValue = leafChild;
                }

                replaceMap.put(oldValue, newValue);
                removeTag(child, leafChild, replaceMap);
            });
        }
    }

    private static class UpAndRebase extends UpAndSomething {

        protected UpAndRebase(boolean saveOld) {
            super(saveOld);
        }

        @Override
        protected void tagsHandler(Map<TreeTagDto, TreeTagDto> replaceMap, NamespaceDto namespace) {
            List<Set<TreeTagDto>> synonyms = namespace.synonyms();
            Set<TaggedFileDto> files = namespace.files();

            replaceMap.keySet().forEach(oldValue -> {
                updateSynonyms(synonyms, oldValue);
                updateFiles(files, oldValue);
            });
        }

        private void updateFiles(Set<TaggedFileDto> files, TreeTagDto oldValue) {
            files.stream()
                    .map(TaggedFileDto::tags)
                    .forEach(tags -> tags.remove(oldValue));
        }

        private void updateSynonyms(List<Set<TreeTagDto>> synonyms, TreeTagDto oldValue) {
            Optional<Set<TreeTagDto>> updatedSynonyms = synonyms.stream()
                    .filter(tags -> tags.remove(oldValue))
                    .findAny();

            updatedSynonyms.ifPresent(tags -> {
                if (tags.size() < 2) {
                    synonyms.remove(tags);
                }
            });
        }
    }

    private static class UpAndMerge extends UpAndSomething {

        private UpAndMerge() {
            this(true);
        }

        protected UpAndMerge(boolean saveOld) {
            super(saveOld);
        }

        @Override
        protected void tagsHandler(Map<TreeTagDto, TreeTagDto> replaceMap, NamespaceDto namespace) {
            List<Set<TreeTagDto>> synonyms = namespace.synonyms();
            Set<TaggedFileDto> files = namespace.files();

            replaceMap.forEach((oldValue, newValue) -> {
                updateSynonyms(synonyms, oldValue, newValue);
                updateFiles(files, oldValue, newValue);
            });
        }

        private void updateFiles(Set<TaggedFileDto> files, TreeTagDto oldValue, TreeTagDto newValue) {
            files.stream()
                    .map(TaggedFileDto::tags)
                    .filter(tags -> tags.remove(oldValue))
                    .forEach(tags -> tags.add(newValue));
        }

        private void updateSynonyms(List<Set<TreeTagDto>> synonyms, TreeTagDto oldValue, TreeTagDto newValue) {
            List<Set<TreeTagDto>> updatedSynonyms = synonyms.stream()
                    .filter(tags -> tags.remove(oldValue) || tags.contains(newValue))
                    .peek(tags -> tags.add(newValue))
                    .toList();

            if (updatedSynonyms.isEmpty()) {
                return;
            }

            List<Set<TreeTagDto>> removingTags = updatedSynonyms.stream()
                    .filter(tags -> tags.size() < 2)
                    .toList();

            synonyms.removeAll(removingTags);
            updatedSynonyms.removeAll(removingTags);

            if (updatedSynonyms.size() > 1) {
                Set<TreeTagDto> unionSet = updatedSynonyms.stream()
                        .flatMap(Set::stream)
                        .collect(() -> Collections.newSetFromMap(new IdentityHashMap<>()), Set::add, Set::addAll);
                synonyms.removeAll(updatedSynonyms);
                synonyms.add(unionSet);
            }
        }
    }
}

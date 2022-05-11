package io.github.ilnurnasybullin.tagfm.core.dto.tag;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.function.BiConsumer;

public class CollisionWalker {

    private final BiConsumer<TreeTagDto, TreeTagDto> hasCollision;
    private final BiConsumer<TreeTagDto, TreeTagDto> noCollision;

    private CollisionWalker(BiConsumer<TreeTagDto, TreeTagDto> hasCollision, BiConsumer<TreeTagDto, TreeTagDto> noCollision) {
        this.hasCollision = hasCollision;
        this.noCollision = noCollision;
    }

    public static CollisionWalker of(BiConsumer<TreeTagDto, TreeTagDto> hasCollision,
                                     BiConsumer<TreeTagDto, TreeTagDto> noCollision) {
        return new CollisionWalker(hasCollision, noCollision);
    }

    public void walk(TreeTagDto primaryTag, TreeTagDto collisionTag) {
        ArrayDeque<TreeTagDto> primaryDeque = new ArrayDeque<>();
        primaryDeque.add(primaryTag);

        ArrayDeque<TreeTagDto> collisionDeque = new ArrayDeque<>();
        collisionDeque.add(collisionTag);

        TreeTagDto tag;
        TreeTagDto[] collision = new TreeTagDto[1];
        while (!primaryDeque.isEmpty()) {
            tag = primaryDeque.poll();
            collision[0] = collisionDeque.poll();

            Map<String, TreeTagDto> leafs = collision[0].children();
            Map.copyOf(tag.children()).forEach((name, primaryChild) -> {
                if (leafs.containsKey(name)) {
                    primaryDeque.add(primaryChild);
                    TreeTagDto collisionChild = leafs.get(name);
                    collisionDeque.add(collisionChild);

                    hasCollision.accept(primaryChild, collisionChild);
                } else {
                    noCollision.accept(primaryChild, collision[0]);
                }
            });
        }
    }
}

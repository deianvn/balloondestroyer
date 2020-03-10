package io.github.deianvn.gameutils.scene;

import com.badlogic.gdx.utils.Array;

public abstract class GroupEntity extends RoomEntity {

    private Array<RoomEntity> entities = new Array<RoomEntity>();

    public GroupEntity(RoomEntity parent) {
        super(parent);
    }

    public void addEntity(RoomEntity entity) {
        entities.add(entity);
        entity.setParent(this);
    }

    public void removeEntity(RoomEntity entity) {
        int i = 0;

        for (RoomEntity e : entities) {
            if (e == entity) {
                entities.removeIndex(i);
            }

            i++;
        }
    }

    public void removeEntity(int index) {
        entities.removeIndex(index);
    }

    public int entitiesCount() {
        return entities.size;
    }

    public Array<RoomEntity> getEntities() {
        return entities;
    }

    @Override
    public void draw() {
        for (RoomEntity entity : entities) {
            entity.draw();
        }
    }

    @Override
    public void update(float deltaTime) {
        for (RoomEntity entity : entities) {
            entity.update(deltaTime);
        }
    }
}

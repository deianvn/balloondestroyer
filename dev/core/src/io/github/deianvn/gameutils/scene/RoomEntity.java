package io.github.deianvn.gameutils.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class RoomEntity extends PositionedEntity {

    private RoomEntity parent;

    private Room<?, ?> room;

    public RoomEntity(RoomEntity parent) {
        setParent(parent);
    }

    public Game getGame() {
        return getRoom().getGame();
    }

    public Room<?, ?> getRoom() {
        return room;
    }

    public RoomEntity getParent() {
        return parent;
    }

    public void setParent(RoomEntity parent) {
        this.parent = parent;

        if (parent != null) {
            this.room = parent.getRoom();
        }
    }

    public SpriteBatch getSpriteBatch() {
        return getRoom().getGame().getSpriteBatch();
    }

    public DecalBatch getDecalBatch() {
        return getRoom().getGame().getDecalBatch();
    }

    @Override
    public float getPositionX() {
        return super.getPositionX() + parent.getPositionX();
    }

    @Override
    public float getPositionY() {
        return super.getPositionY() + parent.getPositionY();
    }

    @Override
    public Vector2 getPosition() {
        Vector2 position = new Vector2(super.getPosition());
        return position.add(parent.getPosition());
    }
}

package net.rizov.balloondestroyer.room.play.entity;

import net.rizov.gameutils.scene.RoomEntity;
import net.rizov.gameutils.scene.TextureEntity;

public class ET extends TextureEntity {

    private float speed = 25;

    public ET(RoomEntity parent) {
        super(parent);
    }

    @Override
    public void update(float deltaTime) {
        shiftPositionX(speed * deltaTime);
    }

    public boolean isAvailable() {
        return getPositionX() < 480;
    }

}

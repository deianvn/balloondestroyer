package io.github.deianvn.balloondestroyer.room.play.entity;

import io.github.deianvn.gameutils.scene.RoomEntity;
import io.github.deianvn.gameutils.scene.TextureEntity;

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

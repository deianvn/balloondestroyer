package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.deianvn.gameutils.scene.RoomEntity;
import io.github.deianvn.gameutils.scene.TextureEntity;

public class Cloud extends TextureEntity {

    private float speed = 0.0f;

    public Cloud(RoomEntity parent, TextureRegion texture) {
        super(parent);
        setTextureRegion(texture);
    }

    @Override
    public void update(float deltaTime) {
        setPositionX(getPositionX() + speed * deltaTime);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isAvailable() {
        if (speed > 0) {
            return getPositionX() < 480;
        } else {
            return getPositionX() + getTextureRegion().getRegionWidth() > 0;
        }
    }

    public void clear() {
        if (speed > 0) {
            setPositionX(-getTextureRegion().getRegionWidth());
        } else {
            setPositionX(getTextureRegion().getRegionWidth() + 480);
        }
    }

}

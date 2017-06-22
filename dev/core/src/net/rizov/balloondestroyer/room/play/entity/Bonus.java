package net.rizov.balloondestroyer.room.play.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.rizov.gameutils.scene.RoomEntity;
import net.rizov.gameutils.scene.TextureEntity;

public class Bonus extends TextureEntity {

    private float speed = 50f;

    private float distance = 30f;

    private float y;

    public Bonus(RoomEntity parent, TextureRegion textureRegion, float x, float y) {
        super(parent);

        setTextureRegion(textureRegion);
        setPositionX(x);
        setPositionY(y);
        this.y = y;
    }

    @Override
    public void update(float deltaTime) {
        setPositionY(getPositionY() + speed * deltaTime);
    }

    public boolean isAvailable() {
        return getPositionY() < y + distance;
    }

}

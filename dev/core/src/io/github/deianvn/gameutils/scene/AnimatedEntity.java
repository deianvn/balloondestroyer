package io.github.deianvn.gameutils.scene;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimatedEntity extends RoomEntity {

    private Animation<TextureRegion> animation;

    private float time = 0f;

    public AnimatedEntity(RoomEntity parent) {
        super(parent);
    }

    public void setFrames(float duration, String name, String atlasName) {
        setFrames(duration, name, atlasName, Animation.PlayMode.LOOP);
    }

    public void setFrames(float duration, String name, String atlasName, Animation.PlayMode playMode) {
        TextureAtlas atlas = getRoom().getAsset(atlasName, TextureAtlas.class);
        Array<TextureRegion> frames = new Array<TextureRegion>();
        int i = 0;

        while (true) {
            TextureRegion region = atlas.findRegion(name + i++);

            if (region == null) {
                break;
            }

            frames.add(region);
        }

        animation = new Animation(duration, frames, playMode);
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(time);
    }

    public float getReminder() {
        return animation.getAnimationDuration() - time;
    }

    @Override
    public void draw() {
        SpriteBatch sb = getRoom().getGame().getSpriteBatch();
        sb.draw(animation.getKeyFrame(time), getPositionX(), getPositionY());
    }

    @Override
    public void update(float deltaTime) {
        time += deltaTime;
    }

    @Override
    protected void computeDimensions() {

    }

    /*
    protected String createRegionIdentification(int index) {
        StringBuilder result = new StringBuilder();

        while (index > 0) {
            index--;
            int remainder = index % 26;
            char digit = (char) (remainder + 97);
            result.insert(0, digit);
            index = (index - remainder) / 26;
        }

        return result.toString();
    }
    */

}

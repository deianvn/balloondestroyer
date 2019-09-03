package io.github.deianvn.balloondestroyer.room.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import io.github.deianvn.balloondestroyer.utils.config.ConfigManager;
import net.rizov.gameutils.scene.RoomEntity;
import net.rizov.gameutils.scene.TextureEntity;

public class Balloon extends TextureEntity {

    private Runnable onBurstCallback;

    private TextureRegion normalTexture;

    private TextureRegion burstTexture;

    private Sound popSound;

    private Ellipse boundaries;

    private float speed = 100;

    private float diversion = 30;

    private float originalPosition;

    private int points = 10;

    private boolean burst = false;

    private float fallingSpeed = 0.0f;

    public Balloon(RoomEntity parent, TextureRegion normalTexture, TextureRegion burstTexture, Sound popSound, Ellipse boundaries) {
        super(parent);

        this.normalTexture = normalTexture;
        this.burstTexture = burstTexture;
        this.popSound = popSound;
        this.boundaries = boundaries;

        clear();
    }

    public void setOnBurstCallback(Runnable onBurstCallback) {
        this.onBurstCallback = onBurstCallback;
    }

    public Runnable getOnBurstCallback() {
        return onBurstCallback;
    }

    @Override
    public void update(float deltaTime) {
        if (burst) {
            fallingSpeed += 500.0f * deltaTime;
            float y = getPositionY();
            y -= fallingSpeed * deltaTime;
            setPositionY(y);
        } else {
            float y = getPositionY();
            y += speed * deltaTime;
            float x = (float) Math.sin(y / 70) * diversion + originalPosition;
            setPositionX(x);
            setPositionY(y);
        }
    }

    @Override
    public void setPositionX(float x) {
        super.setPositionX(x);
        boundaries.setPosition(x + boundaries.width / 2, boundaries.y);
    }

    @Override
    public void setPositionY(float y) {
        super.setPositionY(y);
        boundaries.setPosition(boundaries.x, y + boundaries.height / 2);
    }

    public boolean contains(float x, float y) {
        return boundaries.contains(x, y);
    }

    public void burst() {
        burst = true;

        if (ConfigManager.isSoundEnabled()) {
            popSound.play(1.0f);
        }

        if (ConfigManager.isVibrationEnabled()) {
            Gdx.input.vibrate(25);
        }

        setPositionX(getPositionX() + getTextureRegion().getRegionWidth() / 2);
        setTextureRegion(burstTexture);

        if (onBurstCallback != null) {
            onBurstCallback.run();
        }
    }

    public void clear() {
        burst = false;
        fallingSpeed = 0.0f;
        setTextureRegion(normalTexture);
        setOriginalPosition(diversion + (float) Math.random() * (480 - getTextureRegion().getRegionWidth() - diversion - diversion));
        setPositionY(-getTextureRegion().getRegionHeight());
    }

    public int getPoints() {
        return points;
    }

    public boolean isBurst() {
        return burst;
    }

    public boolean isAvailable() {
        return (burst && getPositionY() > -getTextureRegion().getRegionHeight() - 10) || (!burst && getPositionY() < 800);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDiversion(float diversion) {
        this.diversion = diversion;
    }

    public void setOriginalPosition(float originalPosition) {
        this.originalPosition = originalPosition;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}

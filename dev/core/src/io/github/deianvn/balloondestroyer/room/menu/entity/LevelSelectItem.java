package io.github.deianvn.balloondestroyer.room.menu.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import io.github.deianvn.balloondestroyer.room.entity.Balloon;
import net.rizov.gameutils.scene.RoomEntity;
import net.rizov.gameutils.scene.TextureEntity;

public class LevelSelectItem extends RoomEntity {

    private TextureEntity cloud;

    private Balloon balloon;

    private LevelCounter levelCounter;

    private boolean clouded = true;

    private int hardness;

    private int level;

    public LevelSelectItem(RoomEntity parent) {
        super(parent);
        createEntities();
    }

    public boolean contains(float x, float y) {
        if (clouded) {
            return false;
        }

        if (balloon.contains(x, y)) {
            return true;
        }

        return false;
    }

    public void burst() {
        balloon.burst();
    }

    public boolean isAvailable() {
        return balloon.isAvailable();
    }

    public void setClouded(boolean clouded) {
        this.clouded = clouded;
    }

    public boolean isClouded() {
        return clouded;
    }

    public void setLevel(int level) {
        this.level = level;
        levelCounter.setCount(level);
        positionEntities();
    }

    public int getLevel() {
        return level;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    public int getHardness() {
        return hardness;
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void draw() {
        balloon.draw();

        if (!balloon.isBurst()) {
            levelCounter.draw();
        }

        if (clouded) {
            cloud.draw();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        balloon.update(deltaTime);
    }

    @Override
    protected void onMove() {
        super.onMove();
        positionEntities();
    }

    private void createEntities() {

        TextureAtlas menuAtlas = getRoom().getAsset("menu.atlas", TextureAtlas.class);

        balloon = new Balloon(
                getRoom(),
                menuAtlas.findRegion("balloon-green"),
                menuAtlas.findRegion("balloon-burst-green"),
                Gdx.audio.newSound(Gdx.files.internal("green_pop.wav")),
                new Ellipse(
                        new Vector2(0, 0),
                        51,
                        76));

        balloon.setSpeed(0);
        balloon.setDiversion(0);

        levelCounter = new LevelCounter(getRoom());

        cloud = new TextureEntity(getRoom());
        cloud.setTextureRegion(menuAtlas.findRegion("cloud"));

        positionEntities();

    }

    private void positionEntities() {

        cloud.setPositionX(getPositionX());
        cloud.setPositionY(getPositionY());

        float balloonX = cloud.getPositionX() + (cloud.getWidth() - balloon.getWidth()) / 2;
        balloon.setOriginalPosition(balloonX);
        balloon.setPositionX(balloonX);
        balloon.setPositionY(cloud.getPositionY() + (cloud.getHeight() - balloon.getHeight()) / 2);

        levelCounter.setPositionX(balloonX + (balloon.getWidth() - levelCounter.getWidth()) / 2);
        levelCounter.setPositionY(balloon.getPositionY() + (balloon.getHeight() - levelCounter.getHeight()) / 2 + 13);
    }

}

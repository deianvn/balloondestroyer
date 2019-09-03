package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import net.rizov.gameutils.scene.RoomEntity;
import net.rizov.gameutils.scene.TextureEntity;

public class Credits extends RoomEntity {

    private TextureRegion sunRiseSmallRegion;

    private TextureRegion sunRiseRegion;

    private TextureRegion congratulationsRegion;

    private TextureRegion creditsTitleRegion;

    private TextureRegion creditsRegion;

    private TextureRegion thanksRegion;

    private TextureEntity congratulations;

    private TextureEntity creditsTitle;

    private TextureEntity credits;

    private TextureEntity thanks;

    private TextureEntity sunRise;

    private float speed = 50;

    private Array<RoomEntity> entities = new Array<RoomEntity>();

    private boolean animating = true;

    public Credits(RoomEntity parent) {
        super(parent);
        TextureAtlas playAtlas = getRoom().getAsset("play.atlas", TextureAtlas.class);
        sunRiseSmallRegion = playAtlas.findRegion("dawn-simple-background");
        sunRiseRegion = playAtlas.findRegion("dawn-background");
        congratulationsRegion = playAtlas.findRegion("credits1");
        creditsTitleRegion = playAtlas.findRegion("credits2");
        creditsRegion = playAtlas.findRegion("credits3");
        thanksRegion = playAtlas.findRegion("credits4");
        clear();
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void update(float deltaTime) {
        for (RoomEntity entity : entities) {
            entity.update(deltaTime);
        }
    }

    @Override
    public void draw() {
        if (animating) {
            SpriteBatch sb = getGame().getSpriteBatch();

            sb.draw(sunRiseSmallRegion, 0, 0, 0, 0, 10, 10, 480, 800, 0);

            for (RoomEntity entity : entities) {
                entity.draw();
            }
        }
    }

    public boolean isAvailable() {
        return animating;
    }

    public void clear() {

        thanks = new TextureEntity(getRoom()) {

            private float pause = 5;

            private boolean finished = false;

            @Override
            public void update(float deltaTime) {
                pause -= deltaTime;

                if (!finished && pause < 0) {
                    setTextureRegion((TextureRegion) null);
                    finished = true;
                }

                if (finished && pause < -3) {
                    animating = false;
                }
            }
        };

        thanks.setTextureRegion(thanksRegion);

        creditsTitle = new TextureEntity(getRoom()) {

            @Override
            public void update(float deltaTime) {
                shiftPositionY(speed * deltaTime);
            }
        };

        creditsTitle.setTextureRegion(creditsTitleRegion);

        credits = new TextureEntity(getRoom()) {

            @Override
            public void update(float deltaTime) {
                shiftPositionY(speed * deltaTime);

                if (getPositionY() > 800) {
                    entities.clear();
                    thanks.setPositionX((getRoom().getWidth() - thanks.getWidth()) / 2);
                    thanks.setPositionY((getRoom().getHeight() - thanks.getHeight()) / 2);
                    entities.add(sunRise);
                    entities.add(thanks);
                }
            }
        };

        credits.setTextureRegion(creditsRegion);

        sunRise = new TextureEntity(getRoom()) {

            @Override
            public void update(float deltaTime) {
                shiftPositionY(speed * deltaTime);

                if (getPositionY() > 0) {
                    setPositionY(0);
                    speed = 0;
                }
            }
        };

        sunRise.setTextureRegion(sunRiseRegion);

        congratulations = new TextureEntity(getRoom()) {

            private float pause = 5f;

            @Override
            public void update(float deltaTime) {
                if (pause > 0) {
                    pause -= deltaTime;
                } else {
                    shiftPositionY(speed * deltaTime);

                    if (getPositionY() > 800) {
                        entities.clear();
                        sunRise.setPositionX((getRoom().getWidth() - sunRise.getWidth()) / 2);
                        sunRise.setPositionY((getRoom().getHeight() - sunRise.getHeight()) / 2);
                        creditsTitle.setPositionX((getRoom().getWidth() - creditsTitle.getWidth()) / 2);
                        creditsTitle.setPositionY((getRoom().getHeight() - creditsTitle.getHeight()) / 2);
                        credits.setPositionX((getRoom().getWidth() - credits.getWidth()) / 2);
                        credits.setPositionY((getRoom().getHeight() - credits.getHeight()) / 2);
                        entities.add(sunRise);
                        entities.add(credits);
                        entities.add(creditsTitle);
                        creditsTitle.setPositionY(-creditsTitle.getTextureRegion().getRegionHeight());
                        credits.setPositionY(-creditsTitle.getTextureRegion().getRegionHeight() - credits.getTextureRegion().getRegionHeight() - 150);
                        sunRise.setPositionY(-creditsTitle.getTextureRegion().getRegionHeight() - credits.getTextureRegion().getRegionHeight() - 150 - 800);
                    }
                }
            }
        };

        congratulations.setTextureRegion(congratulationsRegion);
        congratulations.setPositionX((getRoom().getWidth() - congratulations.getWidth()) / 2);
        congratulations.setPositionY((getRoom().getHeight() - congratulations.getHeight()) / 2);
        entities.clear();
        entities.add(congratulations);
    }

}

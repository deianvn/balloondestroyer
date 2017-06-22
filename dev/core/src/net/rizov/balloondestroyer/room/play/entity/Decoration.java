package net.rizov.balloondestroyer.room.play.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.rizov.gameutils.scene.RoomEntity;
import net.rizov.gameutils.scene.TextureEntity;

public class Decoration extends TextureEntity {

    private Time time;

    private TextureRegion dayRegion;

    private TextureRegion duskRegion;

    private TextureRegion nightRegion;

    private TextureRegion morningRegion;

    private TextureRegion etRegion;

    private ET et;

    public Decoration(RoomEntity parent) {
        super(parent);
        TextureAtlas playAtlas = getRoom().getAsset("play.atlas", TextureAtlas.class);
        dayRegion = playAtlas.findRegion("day-background");
        duskRegion = playAtlas.findRegion("dusk-background");
        nightRegion = playAtlas.findRegion("night-background");
        etRegion = playAtlas.findRegion("et");
        morningRegion = playAtlas.findRegion("dawn-background");
    }

    @Override
    public void update(float deltaTime) {
        if (et != null) {
            et.update(deltaTime);

            if (!et.isAvailable()) {
                et = null;
            }
        }
    }

    @Override
    public void draw() {
        super.draw();

        if (et != null) {
            et.draw();
        }
    }

    public void setTime(Time time) {
        if (this.time == time) {
            return;
        }

        this.time = time;

        if (time == Time.day) {
            setTextureRegion(dayRegion);
            et = null;
        } else if (time == Time.dusk) {
            setTextureRegion(duskRegion);
            et = null;
        } else if (time == Time.night) {
            setTextureRegion(nightRegion);
            et = new ET(getRoom());
            et.setTextureRegion(etRegion);
            et.setPositionX(-et.getTextureRegion().getRegionWidth());
            et.setPositionY(420);
        } else {
            setTextureRegion(morningRegion);
            et = null;
        }
    }

}

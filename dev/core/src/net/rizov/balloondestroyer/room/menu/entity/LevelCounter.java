package net.rizov.balloondestroyer.room.menu.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.rizov.balloondestroyer.room.entity.Counter;
import net.rizov.gameutils.scene.RoomEntity;

public class LevelCounter extends Counter {

    public LevelCounter(RoomEntity parent) {
        super(parent);
        setEntities();
    }

    private void setEntities() {

        TextureAtlas menuAtlas = getRoom().getAsset("menu.atlas", TextureAtlas.class);

        setDigits(new TextureRegion[] {
                menuAtlas.findRegion("level-digit0"),
                menuAtlas.findRegion("level-digit1"),
                menuAtlas.findRegion("level-digit2"),
                menuAtlas.findRegion("level-digit3"),
                menuAtlas.findRegion("level-digit4"),
                menuAtlas.findRegion("level-digit5"),
                menuAtlas.findRegion("level-digit6"),
                menuAtlas.findRegion("level-digit7"),
                menuAtlas.findRegion("level-digit8"),
                menuAtlas.findRegion("level-digit9"),
        });

    }

}

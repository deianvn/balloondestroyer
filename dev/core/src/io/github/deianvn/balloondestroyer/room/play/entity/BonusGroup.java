package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import net.rizov.gameutils.scene.RoomEntity;

public class BonusGroup extends RoomEntity {

    private TextureRegion x2Region;

    private TextureRegion x3Region;

    private Array<Bonus> bonuses;

    public BonusGroup(RoomEntity parent) {
        super(parent);
        TextureAtlas playAtlas = parent.getRoom().getAsset("play.atlas", TextureAtlas.class);
        x2Region = playAtlas.findRegion("bonus-x2");
        x3Region = playAtlas.findRegion("bonus-x3");
        bonuses = new Array<Bonus>();
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void update(float deltaTime) {
        int i = 0;

        for (Bonus bonus : bonuses) {
            bonus.update(deltaTime);

            if (!bonus.isAvailable()) {
                bonuses.removeIndex(i);
            }

            i++;
        }
    }

    @Override
    public void draw() {
        for (Bonus bonus : bonuses) {
            bonus.draw();
        }
    }

    public void showBonus(RoomEntity parent, BurstType type, float x, float y) {
        switch (type) {
            case two:
                bonuses.add(new Bonus(parent, x2Region, x, y));
                break;
            case three:
                bonuses.add(new Bonus(parent, x3Region, x, y));
                break;
            default:
        }
    }

    public void clear() {
        bonuses.clear();
    }

}

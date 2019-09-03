package io.github.deianvn.balloondestroyer.room.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import net.rizov.gameutils.scene.RoomEntity;

public abstract class Counter extends RoomEntity {

    private TextureRegion[] digits;

    private float space;

    private int count;

    private float digitWidth;

    private Array<TextureRegion> entities = new Array<TextureRegion>();

    public Counter(RoomEntity parent) {
        super(parent);
    }

    public void setSpace(float space) {
        this.space = space;
        computeDimensions();
    }

    public void setDigits(TextureRegion[] digits) {
        this.digits = digits;
        setCount(count);
        float height = 0;

        for (TextureRegion region : digits) {
            if (height < region.getRegionHeight()) {
                height = region.getRegionHeight();
            }

            if (digitWidth < region.getRegionWidth()) {
                digitWidth = region.getRegionWidth();
            }
        }

        setHeight(height);
    }

    public void setCount(int count) {
        this.count = count;
        entities.clear();

        if (count != 0) {
            while (count > 0) {
                TextureRegion region = digits[count % 10];
                entities.insert(0, region);
                count /= 10;
            }
        } else {
            TextureRegion region = digits[0];
            entities.insert(0, region);
        }

        setWidth(entities.size * digitWidth + space * (entities.size - 1));
        computeDimensions();
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void draw() {

        SpriteBatch sb = getSpriteBatch();
        float x = getPositionX();
        float y = getPositionY();

        for (TextureRegion region : entities) {
            sb.draw(region, x, y);
            x += space + digitWidth;
        }

    }

}

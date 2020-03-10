package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.deianvn.gameutils.scene.RoomEntity;

public class WaveTitle extends RoomEntity {

    private static final float hMargin = 3;

    private static final float vMargin = 5;

    private int wave;

    private TextureRegion waveRegion;

    private Array<TextureRegion> currentWave;

    private TextureRegion waveNumbersRegions[];

    private float waveX;

    private float waveY;

    private float numberX;

    private float numberY;

    private float speed = 600f;

    private float pause = 1f;

    public WaveTitle(RoomEntity parent) {
        super(parent);

        TextureAtlas playAtlas = getRoom().getAsset("play.atlas", TextureAtlas.class);
        waveRegion = playAtlas.findRegion("wave");
        currentWave = new Array<TextureRegion>();

        waveNumbersRegions = new TextureRegion[]{
                playAtlas.findRegion("wave-digit0"),
                playAtlas.findRegion("wave-digit1"),
                playAtlas.findRegion("wave-digit2"),
                playAtlas.findRegion("wave-digit3"),
                playAtlas.findRegion("wave-digit4"),
                playAtlas.findRegion("wave-digit5"),
                playAtlas.findRegion("wave-digit6"),
                playAtlas.findRegion("wave-digit7"),
                playAtlas.findRegion("wave-digit8"),
                playAtlas.findRegion("wave-digit9"),
        };
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void update(float deltaTime) {
        pause -= deltaTime;

        if (pause < 0) {
            float shiftX = speed * deltaTime;
            waveX += shiftX;
            numberX += shiftX;
        }
    }

    @Override
    public void draw() {

        SpriteBatch sb = getGame().getSpriteBatch();

        sb.draw(waveRegion, waveX, waveY);
        float tempNumberX = numberX;

        for (TextureRegion region : currentWave) {
            sb.draw(region, tempNumberX, numberY);
            tempNumberX += region.getRegionWidth() + hMargin;
        }
    }

    public void setWave(int wave) {
        this.wave = wave;
        updateCurrentWave();
    }

    public boolean isAvailable() {
        return waveX < 480 && numberX < 480;
    }

    private void updateCurrentWave() {
        pause = 1f;
        currentWave.clear();
        int tempWave = wave;

        do {
            currentWave.insert(0, waveNumbersRegions[tempWave % 10]);
            tempWave /= 10;
        } while (tempWave > 0);

        float waveWidth = waveRegion.getRegionWidth();
        Vector2 numberDimensions = getNumberDimensions();

        numberX = 480 / 2 - numberDimensions.x / 2;
        numberY = 800 / 2 - (numberDimensions.y + vMargin + waveRegion.getRegionHeight()) / 2;
        waveX = 480 / 2 - waveWidth / 2;
        waveY = numberY + numberDimensions.y + vMargin;

    }

    private Vector2 getNumberDimensions() {
        float width = 0f;
        float height = 0f;

        for (TextureRegion region : currentWave) {
            width += region.getRegionWidth();

            if (height < region.getRegionHeight()) {
                height = region.getRegionHeight();
            }
        }

        width += (currentWave.size - 1) * hMargin;

        return new Vector2(width, height);
    }

}

package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.deianvn.balloondestroyer.room.play.PlayRoomEvent;
import io.github.deianvn.balloondestroyer.utils.config.ConfigManager;
import io.github.deianvn.gameutils.scene.RoomEntity;
import io.github.deianvn.gameutils.scene.TextureEntity;

public class GameOverMenu extends TextureEntity {

    private Circle restartArea = new Circle(new Vector2(214, 49), 35f);

    private Circle quitArea = new Circle(new Vector2(313, 49), 35f);

    private TextureRegion[] scoreNumbersRegions;

    private Array<TextureRegion> currentScore = new Array<TextureRegion>();

    private int score;

    private float scoreX;

    private float scoreY;

    private Sound tapSound;

    public GameOverMenu(RoomEntity parent) {
        super(parent);

        TextureAtlas playAtlas = getRoom().getAsset("play.atlas", TextureAtlas.class);
        tapSound = getRoom().getAsset("yellow_pop.wav", Sound.class);
        setTextureRegion(playAtlas.findRegion("dialog-game-over"));

        scoreNumbersRegions = new TextureRegion[]{
                playAtlas.findRegion("dialog-digit0"),
                playAtlas.findRegion("dialog-digit1"),
                playAtlas.findRegion("dialog-digit2"),
                playAtlas.findRegion("dialog-digit3"),
                playAtlas.findRegion("dialog-digit4"),
                playAtlas.findRegion("dialog-digit5"),
                playAtlas.findRegion("dialog-digit6"),
                playAtlas.findRegion("dialog-digit7"),
                playAtlas.findRegion("dialog-digit8"),
                playAtlas.findRegion("dialog-digit9")
        };
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw() {
        super.draw();

        SpriteBatch sb = getSpriteBatch();
        float tempScoreX = scoreX;

        for (TextureRegion region : currentScore) {
            sb.draw(region, tempScoreX, scoreY);
            tempScoreX += 2 + region.getRegionWidth();
        }
    }

    public void center() {
        setPositionX((getRoom().getWidth() - getWidth()) / 2);
        setPositionY((getRoom().getHeight() - getHeight()) / 2);

        scoreX = getPositionX() + 174;
        scoreY = getPositionY() + 234;
    }

    public PlayRoomEvent getItem(float x, float y) {
        x -= getPositionX();
        y -= getPositionY();

        if (restartArea.contains(x, y)) {
            onClick();
            return PlayRoomEvent.restart;
        } else if (quitArea.contains(x, y)) {
            onClick();
            return PlayRoomEvent.quit;
        }


        return null;
    }

    public void setScore(int score) {
        this.score = score;
        updateCurrentScore();
    }

    private void updateCurrentScore() {
        currentScore.clear();
        int tempScore = score;

        do {
            currentScore.insert(0, scoreNumbersRegions[tempScore % 10]);
            tempScore /= 10;
        } while (tempScore > 0);
    }

    private void onClick() {
        if (ConfigManager.isSoundEnabled()) {
            tapSound.play(1.0f);
        }

        if (ConfigManager.isVibrationEnabled()) {
            Gdx.input.vibrate(25);
        }
    }

}

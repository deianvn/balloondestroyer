package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import io.github.deianvn.gameutils.scene.RoomEntity;

public class ScoreBoard extends RoomEntity {

    private int score = 0;

    private int lives = 0;

    private float scoreNumbersSpace = 2;

    private TextureRegion scoreBoardRegion;

    private TextureRegion[] scoreNumbersRegions;

    private TextureRegion lifeRegion;

    private Array<TextureRegion> currentScore = new Array<TextureRegion>();

    public ScoreBoard(RoomEntity parent) {
        super(parent);

        TextureAtlas playAtlas = getRoom().getAsset("play.atlas", TextureAtlas.class);
        scoreBoardRegion = playAtlas.findRegion("score-panel");

        scoreNumbersRegions = new TextureRegion[]{
                playAtlas.findRegion("score-digit0"),
                playAtlas.findRegion("score-digit1"),
                playAtlas.findRegion("score-digit2"),
                playAtlas.findRegion("score-digit3"),
                playAtlas.findRegion("score-digit4"),
                playAtlas.findRegion("score-digit5"),
                playAtlas.findRegion("score-digit6"),
                playAtlas.findRegion("score-digit7"),
                playAtlas.findRegion("score-digit8"),
                playAtlas.findRegion("score-digit9")
        };

        lifeRegion = playAtlas.findRegion("life");
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw() {
        SpriteBatch sb = getGame().getSpriteBatch();

        sb.draw(scoreBoardRegion, 0, 740);
        float x = 148;
        float y = 760;

        for (TextureRegion digit : currentScore) {
            sb.draw(digit, x, y);
            x += digit.getRegionWidth() + scoreNumbersSpace;
        }

        x = 343;
        y = 754;
        int tempLives = lives;

        if (tempLives > 3) {
            tempLives = 3;
        }

        while (tempLives > 0) {
            sb.draw(lifeRegion, x, y);
            tempLives--;
            x += 27;
        }
    }

    public void setScore(int score) {
        this.score = score;
        updateCurrentScore();
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    private void updateCurrentScore() {
        currentScore.clear();
        int tempScore = score;

        do {
            currentScore.insert(0, scoreNumbersRegions[tempScore % 10]);
            tempScore /= 10;
        } while (tempScore > 0);
    }

}

package net.rizov.balloondestroyer.room.play;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import net.rizov.balloondestroyer.room.play.entity.Time;
import net.rizov.gameutils.scene.Game;
import net.rizov.gameutils.scene.Room;

public abstract class PlayRoom extends Room<PlayRoomStatus, PlayRoomEvent> implements InputProcessor {

    private int wave;

    private int score;

    private int lives;

    private Time time;

    private int hardness;

    public PlayRoom(Game game) {
        super(game);
        setStatus(PlayRoomStatus.playing);
        wave = 1;
        score = 0;
        lives = 3;
        time = Time.day;
    }

    @Override
    public void update(float deltaTime) {
        switch (getStatus()) {
            case playing:
                updatePlaying(deltaTime);
                break;
            case paused:
                updatePaused(deltaTime);
                break;
            case losing:
                updateLosing(deltaTime);
                break;
            case lost:
                updateLost(deltaTime);
                break;
            case winning:
                updateWinning(deltaTime);
                break;
            case won:
                updateWon(deltaTime);
                break;
            default:
                break;
        }
    }

    @Override
    public void draw() {
        switch (getStatus()) {
            case playing:
                drawPlaying();
                break;
            case paused:
                drawPaused();
                break;
            case losing:
                drawLosing();
                break;
            case lost:
                drawLost();
                break;
            case winning:
                drawWinning();
                break;
            case won:
                drawWon();
                break;
            default:
                break;
        }
    }

    public int getWave() {
        return wave;
    }

    protected void setWave(int wave) {
        this.wave = wave;

        if (wave < 34) {
            setTime(Time.day);
        } else if (wave < 67) {
            setTime(Time.dusk);
        } else {
            setTime(Time.night);
        }
    }

    public int getScore() {
        return score;
    }

    protected void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    protected void setLives(int lives) {
        this.lives = lives;
    }

    public Time getTime() {
        return time;
    }

    protected void setTime(Time time) {
        this.time = time;
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    public int getBalloonsNumber() {
        if (time == Time.day) {
            return 10 * hardness;
        } else if (time == Time.dusk) {
            return 15 * hardness;
        } else if (time == Time.night && getWave() < 100) {
            return 20 * hardness;
        } else {
            return 5 * hardness + 10 * hardness + 20 * hardness + 50 * hardness;
        }
    }

    @Override
    protected void pause() {
        if (getStatus() != PlayRoomStatus.lost && getStatus() != PlayRoomStatus.won) {
            setStatus(PlayRoomStatus.paused);
        }
    }

    @Override
    protected void resume() {
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (getStatus()) {
            case playing:
                return handleKeyDownPlaying(keycode);
            case paused:
                return handleKeyDownPaused(keycode);
            case lost:
                return handleKeyDownLost(keycode);
            case won:
                return handleKeyDownWon(keycode);
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 coordinates = new Vector3(screenX, screenY, 0);
        coordinates = getViewport().unproject(coordinates);

        switch (getStatus()) {
            case playing:
                return handleTouchDownPlaying(coordinates.x, coordinates.y);
            case paused:
                return handleTouchDownPaused(coordinates.x, coordinates.y);
            case lost:
                return handleTouchDownLost(coordinates.x, coordinates.y);
            case won:
                return handleTouchDownWon(coordinates.x, coordinates.y);
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    protected void resize(int width, int height) {
    }

    @Override
    protected void hide() {
    }

    protected abstract void updatePlaying(float deltaTime);

    protected abstract void updatePaused(float deltaTime);

    protected abstract void updateLosing(float deltaTime);

    protected abstract void updateLost(float deltaTime);

    protected abstract void updateWinning(float deltaTime);

    protected abstract void updateWon(float deltaTime);

    protected abstract void drawPlaying();

    protected abstract void drawPaused();

    protected abstract void drawLosing();

    protected abstract void drawLost();

    protected abstract void drawWinning();

    protected abstract void drawWon();

    protected abstract boolean handleKeyDownPlaying(int keycode);

    protected abstract boolean handleKeyDownPaused(int keycode);

    protected abstract boolean handleKeyDownLost(int keycode);

    protected abstract boolean handleKeyDownWon(int keycode);

    protected abstract boolean handleTouchDownPlaying(float x, float y);

    protected abstract boolean handleTouchDownPaused(float x, float y);

    protected abstract boolean handleTouchDownLost(float x, float y);

    protected abstract boolean handleTouchDownWon(float x, float y);

}

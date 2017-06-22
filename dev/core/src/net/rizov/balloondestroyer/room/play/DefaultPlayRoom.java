package net.rizov.balloondestroyer.room.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import net.rizov.balloondestroyer.utils.config.ConfigManager;
import net.rizov.balloondestroyer.room.menu.MenuRoom;
import net.rizov.balloondestroyer.room.play.entity.BonusGroup;
import net.rizov.balloondestroyer.room.play.entity.Burst;
import net.rizov.balloondestroyer.room.play.entity.Credits;
import net.rizov.balloondestroyer.room.play.entity.Decoration;
import net.rizov.balloondestroyer.room.play.entity.GameOverMenu;
import net.rizov.balloondestroyer.room.play.entity.PauseMenu;
import net.rizov.balloondestroyer.room.play.entity.ScoreBoard;
import net.rizov.balloondestroyer.room.play.entity.Time;
import net.rizov.balloondestroyer.room.play.entity.Wave;
import net.rizov.balloondestroyer.utils.save.BalloonDestroyerSaveManager;
import net.rizov.gameutils.scene.Game;
import net.rizov.gameutils.scene.TextureEntity;

public class DefaultPlayRoom extends PlayRoom {

    private int initialWave = 1;

    private ScoreBoard scoreBoard;

    private PauseMenu pauseMenu;

    private GameOverMenu gameOverMenu;

    private Decoration decoration;

    private BonusGroup bonusGroup;

    private Wave wave;

    private Credits credits;

    private Sound oneUpSound;

    private TextureEntity cheatEntity;

    private boolean won;

    private int livesWon = 0;

    public DefaultPlayRoom(Game game) {
        super(game);
    }

    public void setInitialWave(int initialWave) {
        this.initialWave = initialWave;
        setWave(initialWave);
    }

    public int getInitialWave() {
        return initialWave;
    }

    @Override
    protected InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void setWave(int wave) {
        super.setWave(wave);
        this.wave.setWave(getWave());
        this.wave.setNumber(getBalloonsNumber());
    }

    @Override
    public void setLives(int lives) {
        super.setLives(lives);
        scoreBoard.setLives(getLives());
    }

    @Override
    public void setScore(int score) {
        super.setScore(score);
        scoreBoard.setScore(getScore());
        gameOverMenu.setScore(getScore());
    }

    @Override
    public void setTime(Time time) {
        super.setTime(time);
        wave.setTime(time);
        decoration.setTime(getTime());
    }

    public void restart() {
        save();
        setWave(getInitialWave());
        setScore(0);
        setLives(3);
        bonusGroup.clear();
        credits.clear();
        setStatus(PlayRoomStatus.playing);
    }

    @Override
    protected void updatePlaying(float deltaTime) {
        decoration.update(deltaTime);
        wave.update(deltaTime);
        int escapedBalloons = wave.escapedBalloons();

        if (escapedBalloons > 0) {
            setLives(getLives() - escapedBalloons);

            if (getLives() < 1) {
                finish();
                return;
            }
        }

        bonusGroup.update(deltaTime);

        if (wave.isFinished()) {
            if (getWave() == 100) {
                win();
            } else {
                setWave(getWave() + 1);
            }
        }
    }

    @Override
    protected void updatePaused(float deltaTime) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateLosing(float deltaTime) {

    }

    @Override
    protected void updateLost(float deltaTime) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void updateWinning(float deltaTime) {

    }

    @Override
    protected void updateWon(float deltaTime) {
        credits.update(deltaTime);

        if (!credits.isAvailable()) {
            setTime(Time.morning);
            won = true;
            finish();
        }
    }

    @Override
    protected void drawPlaying() {
        decoration.draw();
        wave.draw();
        bonusGroup.draw();
        scoreBoard.draw();
    }

    @Override
    protected void drawPaused() {
        drawPlaying();
        pauseMenu.draw();
    }

    @Override
    protected void drawLosing() {
        drawPlaying();
    }

    @Override
    protected void drawLost() {
        drawPlaying();
        gameOverMenu.draw();

        if (won && getHardness() == 4) {
            cheatEntity.draw();
        }
    }

    @Override
    protected void drawWinning() {

    }

    @Override
    protected void drawWon() {
        credits.draw();
    }

    @Override
    protected boolean handleKeyDownPlaying(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            pause();

            return true;
        }

        return false;
    }

    @Override
    protected boolean handleKeyDownPaused(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            setStatus(PlayRoomStatus.playing);

            return true;
        }

        return false;
    }

    @Override
    protected boolean handleKeyDownLost(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            toMainMenu();
        }

        return false;
    }

    @Override
    protected boolean handleKeyDownWon(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            setTime(Time.morning);
            setStatus(PlayRoomStatus.lost);
        }

        return false;
    }

    @Override
    protected boolean handleTouchDownPlaying(float x, float y) {

        if (y > 740) {
            if (x >= 420 && getStatus() == PlayRoomStatus.playing) {
                pause();
                return true;
            }

            return false;
        }

        Burst burst = wave.burst(x, y);

        if (burst != null) {
            setScore(getScore() + burst.getScore());
            bonusGroup.showBonus(this, burst.getType(), x, y);

            if (getScore() > 10000 * (livesWon + 1)) {
                setLives(getLives() + 1);
                livesWon++;

                if (ConfigManager.isSoundEnabled()) {
                    oneUpSound.play(1.0f);
                }

                if (ConfigManager.isVibrationEnabled()) {
                    Gdx.input.vibrate(new long[]{50, 50, 300}, -1);
                }
            }
        }

        return false;
    }

    @Override
    protected boolean handleTouchDownPaused(float x, float y) {

        PlayRoomEvent event = pauseMenu.getItem(x, y);

        if (event == null) {
            return false;
        }

        event.score = getScore();
        triggerEvent(event);

        switch (event) {
            case resume:
                setStatus(PlayRoomStatus.playing);
                break;
            case restart:
                restart();
                break;
            case quit:
                toMainMenu();
                break;
            default:
        }

        return false;
    }

    @Override
    protected boolean handleTouchDownLost(float x, float y) {
        PlayRoomEvent event = gameOverMenu.getItem(x, y);

        if (event == null) {
            return false;
        }

        event.score = getScore();
        triggerEvent(event);

        switch (event) {
            case restart:
                restart();
                break;
            case quit:
                toMainMenu();
                break;
            default:
        }

        return false;
    }

    @Override
    protected boolean handleTouchDownWon(float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void prepare() {

    }

    @Override
    protected void loadData() {
        loadAsset("play.atlas", TextureAtlas.class);
        loadAsset("red_pop.wav", Sound.class);
        loadAsset("blue_pop.wav", Sound.class);
        loadAsset("yellow_pop.wav", Sound.class);
        loadAsset("green_pop.wav", Sound.class);
        loadAsset("escape.wav", Sound.class);
        loadAsset("oneup.wav", Sound.class);
    }

    @Override
    protected void show() {
        scoreBoard = new ScoreBoard(this);
        scoreBoard.setLives(3);
        scoreBoard.setScore(getScore());

        pauseMenu = new PauseMenu(this);
        pauseMenu.setPositionX((getWidth() - pauseMenu.getWidth()) / 2);
        pauseMenu.setPositionY((getHeight() - pauseMenu.getHeight()) / 2);
        gameOverMenu = new GameOverMenu(this);
        gameOverMenu.center();
        gameOverMenu.setScore(getScore());

        decoration = new Decoration(this);
        decoration.setTime(getTime());

        bonusGroup = new BonusGroup(this);

        credits = new Credits(this);
        credits.clear();

        oneUpSound = getAsset("oneup.wav", Sound.class);

        cheatEntity = new TextureEntity(this) {

            @Override
            public void update(float deltaTime) {

            }
        };

        cheatEntity.setTextureRegion(getAsset("play.atlas", TextureAtlas.class).findRegion("credits-cheat"));
        cheatEntity.setPositionX((getWidth() - cheatEntity.getWidth()) / 2);
        cheatEntity.setPositionY((getHeight() - cheatEntity.getHeight()) / 2);
        cheatEntity.setPositionY(10);

        wave = new Wave(this, getTime());
        wave.setHardness(getHardness());
        setWave(1);
        setLives(3);
        won = false;
    }

    @Override
    protected void pause() {
        super.pause();
        save();

        PlayRoomEvent event = PlayRoomEvent.pause;
        event.score = getScore();
        triggerEvent(event);
    }

    @Override
    protected void hide() {
        super.hide();
        save();

        PlayRoomEvent event = PlayRoomEvent.quit;
        event.score = getScore();
        triggerEvent(event);
    }

    private void toMainMenu() {
        save();
        getGame().setCurrentRoom(new MenuRoom(getGame()));
    }

    private void win() {
        setStatus(PlayRoomStatus.won);

        PlayRoomEvent event = PlayRoomEvent.win;
        event.score = getScore();
        triggerEvent(event);
    }

    private void finish() {
        setStatus(PlayRoomStatus.lost);

        PlayRoomEvent event = PlayRoomEvent.lose;
        event.score = getScore();
        triggerEvent(event);
    }

    private void save() {
        int score = getScore();

        if (score > BalloonDestroyerSaveManager.getHighScore()) {
            BalloonDestroyerSaveManager.setHighScore(score);
        }

        if (wave.getWave() > BalloonDestroyerSaveManager.getWave(wave.getHardness())) {
            BalloonDestroyerSaveManager.setWave(wave.getWave(), wave.getHardness());
        }

        BalloonDestroyerSaveManager.save();
        PlayRoomEvent event = PlayRoomEvent.submitScore;
        event.score = getScore();
        triggerEvent(event);
    }

}

package io.github.deianvn.balloondestroyer.room.menu.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.deianvn.balloondestroyer.room.entity.Balloon;
import io.github.deianvn.balloondestroyer.room.menu.MenuRoom;
import io.github.deianvn.balloondestroyer.room.menu.MenuRoomEvent;
import io.github.deianvn.balloondestroyer.room.menu.MenuRoomStatus;
import io.github.deianvn.balloondestroyer.utils.config.ConfigManager;
import io.github.deianvn.balloondestroyer.utils.save.BalloonDestroyerSaveManager;
import io.github.deianvn.gameutils.scene.RoomEntity;

public class MainMenu extends RoomEntity {

    private TextureRegion homeRegion;

    private Balloon selectedBalloon;

    private Balloon easyBalloon;

    private Balloon mediumBalloon;

    private Balloon hardBalloon;

    private TextureRegion xRegion;

    private Rectangle soundArea = new Rectangle(395, 65, 58, 46);

    private Rectangle vibrateArea = new Rectangle(395, 18, 58, 46);

    private Circle exitArea = new Circle(65, 3, 81);

    private Ellipse leaderBoardArea = new Ellipse(258, 450, 355, 159);

    private Ellipse rateArea = new Ellipse(122, 565, 208, 96);

    private Ellipse rulesArea = new Ellipse(346, 596, 220, 103);

    private Sound tapSound;

    private Cheat cheat;

    public MainMenu(RoomEntity parent) {
        super(parent);
        createEntities();
    }

    @Override
    protected void computeDimensions() {

    }

    public void clear() {
        createEntities();
    }

    @Override
    public void draw() {
        SpriteBatch sb = getRoom().getGame().getSpriteBatch();
        sb.draw(homeRegion, 0, 0);

        if (ConfigManager.isSoundEnabled()) {
            sb.draw(xRegion, 412, 74);
        }

        if (ConfigManager.isVibrationEnabled()) {
            sb.draw(xRegion, 412, 28);
        }

        easyBalloon.draw();
        mediumBalloon.draw();
        hardBalloon.draw();
    }

    @Override
    public void update(float deltaTime) {
        cheat.update(deltaTime);

        if (selectedBalloon != null) {
            selectedBalloon.update(deltaTime);

            if (!selectedBalloon.isAvailable()) {
                int hardness = 2;

                if (selectedBalloon == easyBalloon) {
                    hardness = 2;
                } else if (selectedBalloon == mediumBalloon) {
                    hardness = 3;
                } else if (selectedBalloon == hardBalloon) {
                    hardness = 4;
                }

                int wave = BalloonDestroyerSaveManager.getWave(hardness);

                if (wave < 1) {
                    wave = 1;
                }

                if (cheat.isCheatEnabled() && wave < cheat.getSkipWaves() + 1) {
                    wave = cheat.getSkipWaves() + 1;
                }

                getMenuRoom().showLevelSelectMenu(hardness, wave);
            }
        }
    }

    public void click(float x, float y) {
        cheat.click(x, y);

        if (soundArea.contains(x, y)) {
            ConfigManager.setSoundEnabled(!ConfigManager.isSoundEnabled());

            if (ConfigManager.isSoundEnabled()) {
                tapSound.play(1.0f);
            }

            ConfigManager.save();
        } else if (vibrateArea.contains(x, y)) {
            ConfigManager.setVibrationEnabled(!ConfigManager.isVibrationEnabled());

            if (ConfigManager.isVibrationEnabled()) {
                Gdx.input.vibrate(25);
            }

            ConfigManager.save();
        } else if (leaderBoardArea.contains(x, y)) {
            onClick();
            getMenuRoom().triggerEvent(MenuRoomEvent.showLeaderBoard);
        } else if (rateArea.contains(x, y)) {
            onClick();
            getMenuRoom().triggerEvent(MenuRoomEvent.rate);
            //Gdx.net.openURI("https://play.google.com/store/apps/details?id=io.github.deianvn.balloondestroyer.android");
        } else if (rulesArea.contains(x, y)) {
            onClick();
            getMenuRoom().show(MenuRoomStatus.rulesPage);
        } else if (exitArea.contains(x, y)) {
            onClick();
            getGame().quit();
        } else if (selectedBalloon == null) {
            if (easyBalloon.contains(x, y)) {
                selectedBalloon = easyBalloon;
                selectedBalloon.burst();
            } else if (mediumBalloon.contains(x, y)) {
                selectedBalloon = mediumBalloon;
                selectedBalloon.burst();
            } else if (hardBalloon.contains(x, y)) {
                selectedBalloon = hardBalloon;
                selectedBalloon.burst();
            }
        }
    }

    public void keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            Gdx.app.exit();
        }
    }

    private void createEntities() {
        TextureAtlas menuAtlas = getRoom().getAsset("menu.atlas", TextureAtlas.class);
        tapSound = getRoom().getAsset("yellow_pop.wav", Sound.class);

        homeRegion = menuAtlas.findRegion("home-background");
        xRegion = menuAtlas.findRegion("tick");

        easyBalloon = new Balloon(
                getRoom(),
                menuAtlas.findRegion("balloon-red"),
                menuAtlas.findRegion("balloon-burst-red"),
                Gdx.audio.newSound(Gdx.files.internal("red_pop.wav")),
                new Ellipse(
                        new Vector2(0, 0),
                        93,
                        121));

        mediumBalloon = new Balloon(
                getRoom(),
                menuAtlas.findRegion("balloon-yellow"),
                menuAtlas.findRegion("balloon-burst-yellow"),
                Gdx.audio.newSound(Gdx.files.internal("yellow_pop.wav")),
                new Ellipse(
                        new Vector2(0, 0),
                        69,
                        102));

        hardBalloon = new Balloon(
                getRoom(),
                menuAtlas.findRegion("balloon-green"),
                menuAtlas.findRegion("balloon-burst-green"),
                Gdx.audio.newSound(Gdx.files.internal("green_pop.wav")),
                new Ellipse(
                        new Vector2(0, 0),
                        51,
                        76));

        easyBalloon.setSpeed(0);
        easyBalloon.setDiversion(0);
        easyBalloon.setOriginalPosition(63);
        easyBalloon.setPositionX(63);
        easyBalloon.setPositionY(115);
        mediumBalloon.setSpeed(0);
        mediumBalloon.setDiversion(0);
        mediumBalloon.setOriginalPosition(219);
        mediumBalloon.setPositionX(219);
        mediumBalloon.setPositionY(191);
        hardBalloon.setSpeed(0);
        hardBalloon.setDiversion(0);
        hardBalloon.setOriginalPosition(374);
        hardBalloon.setPositionX(374);
        hardBalloon.setPositionY(188);

        cheat = new Cheat(getRoom());
    }

    private void onClick() {
        if (ConfigManager.isSoundEnabled()) {
            tapSound.play(1.0f);
        }

        if (ConfigManager.isVibrationEnabled()) {
            Gdx.input.vibrate(25);
        }
    }

    private MenuRoom getMenuRoom() {
        return (MenuRoom) getRoom();
    }
}

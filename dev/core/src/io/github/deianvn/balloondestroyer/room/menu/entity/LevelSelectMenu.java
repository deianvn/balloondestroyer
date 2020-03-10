package io.github.deianvn.balloondestroyer.room.menu.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Array;
import io.github.deianvn.balloondestroyer.room.menu.MenuRoom;
import io.github.deianvn.balloondestroyer.room.menu.MenuRoomStatus;
import io.github.deianvn.balloondestroyer.room.play.DefaultPlayRoom;
import io.github.deianvn.balloondestroyer.utils.config.ConfigManager;
import io.github.deianvn.gameutils.scene.RoomEntity;
import io.github.deianvn.gameutils.scene.TextureEntity;

public class LevelSelectMenu extends RoomEntity {

    private TextureEntity background;

    private Sound tapSound;

    private Array<LevelSelectItem> items = new Array<LevelSelectItem>();

    private Circle backArea = new Circle(65, 3, 81);

    private LevelSelectItem selectedItem;

    public LevelSelectMenu(RoomEntity parent) {
        super(parent);
        createEntities();
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void draw() {
        background.draw();

        for (LevelSelectItem item : items) {
            item.draw();
        }
    }

    @Override
    public void update(float deltaTime) {
        if (selectedItem != null) {
            selectedItem.update(deltaTime);

            if (!selectedItem.isAvailable()) {
                startGame(selectedItem.getHardness(), selectedItem.getLevel());
            }
        }
    }

    public void startGame(int hardness, int level) {
        DefaultPlayRoom playRoom = new DefaultPlayRoom(getRoom().getGame());
        playRoom.setHardness(hardness);
        getRoom().getGame().setCurrentRoom(playRoom);
        playRoom.setInitialWave(level);
    }

    public void click(float x, float y) {

        if (selectedItem != null) {
            return;
        }

        if (backArea.contains(x, y)) {
            getMenuRoom().show(MenuRoomStatus.mainMenu);
            onClick();

            return;
        }

        for (LevelSelectItem item : items) {
            if (item.contains(x, y)) {
                item.burst();
                selectedItem = item;
                break;
            }
        }

    }

    public void keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            getMenuRoom().show(MenuRoomStatus.mainMenu);
        }
    }

    public void make(int hardness, int wave) {
        items.clear();

        if (wave == 0) {
            wave = 1;
        }

        float x = 50;
        float y = 95;
        int mul = 1;
        int currentWave = 1;

        while (currentWave <= 91) {

            LevelSelectItem item = new LevelSelectItem(getRoom());
            item.setHardness(hardness);

            if (currentWave <= wave) {
                item.setClouded(false);
                item.setLevel(currentWave);
            } else {
                if (currentWave - wave < 10) {
                    item.setClouded(false);
                    item.setLevel(wave);
                } else {
                    item.setClouded(true);
                    item.setLevel(currentWave);
                }
            }

            item.setPositionX(x);
            item.setPositionY(y);
            x += mul * 210;

            if (mul < 0) {
                y += 115;
            }

            mul *= -1;
            items.add(item);
            currentWave += 10;
        }

    }

    private void createEntities() {
        background = new TextureEntity(getRoom());
        background.setTextureRegion("levels-background", "menu.atlas");

        tapSound = getRoom().getAsset("yellow_pop.wav", Sound.class);
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

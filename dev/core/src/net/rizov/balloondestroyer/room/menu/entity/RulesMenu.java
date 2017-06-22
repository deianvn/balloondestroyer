package net.rizov.balloondestroyer.room.menu.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Circle;
import net.rizov.balloondestroyer.room.menu.MenuRoom;
import net.rizov.balloondestroyer.room.menu.MenuRoomStatus;
import net.rizov.balloondestroyer.utils.config.ConfigManager;
import net.rizov.gameutils.scene.RoomEntity;
import net.rizov.gameutils.scene.TextureEntity;

public class RulesMenu extends RoomEntity {

    private TextureEntity rulesTexture;

    private Sound tapSound;

    private Circle backArea = new Circle(65, 3, 81);

    public RulesMenu(RoomEntity parent) {
        super(parent);
        createEntities();
    }

    public void click(float x, float y) {

        if (backArea.contains(x, y)) {
            getMenuRoom().show(MenuRoomStatus.mainMenu);
            onClick();
        }

    }

    public void keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            getMenuRoom().show(MenuRoomStatus.mainMenu);
        }
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void draw() {
        rulesTexture.draw();
    }

    private void createEntities() {
        rulesTexture = new TextureEntity(this);
        rulesTexture.setTextureRegion("rules-background", "menu.atlas");
        tapSound = getRoom().getAsset("yellow_pop.wav", Sound.class);
    }

    private MenuRoom getMenuRoom() {
        return (MenuRoom)getRoom();
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

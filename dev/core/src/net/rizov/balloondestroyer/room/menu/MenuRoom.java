package net.rizov.balloondestroyer.room.menu;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import net.rizov.balloondestroyer.room.menu.entity.LevelSelectMenu;
import net.rizov.balloondestroyer.room.menu.entity.MainMenu;
import net.rizov.balloondestroyer.room.menu.entity.RulesMenu;
import net.rizov.gameutils.scene.Game;
import net.rizov.gameutils.scene.Room;

public class MenuRoom extends Room<MenuRoomStatus, MenuRoomEvent> implements InputProcessor {

    private MainMenu mainMenu;

    private LevelSelectMenu levelSelectMenu;

    private RulesMenu rulesMenu;

    public MenuRoom(Game game) {
        super(game);
    }

    public void showLevelSelectMenu(int hardness, int wave) {
        show(MenuRoomStatus.levelSelection);
        levelSelectMenu.make(hardness, wave);
    }

    public void show(MenuRoomStatus status) {

        switch (status) {
            case mainMenu:
                mainMenu = new MainMenu(this);
                break;
            case levelSelection:
                levelSelectMenu = new LevelSelectMenu(this);
                break;
        }

        setStatus(status);
    }

    @Override
    protected InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void update(float deltaTime) {
        switch (getStatus()) {
            case mainMenu: {
                mainMenu.update(deltaTime);
                break;
            }
            case levelSelection: {
                levelSelectMenu.update(deltaTime);
                break;
            }
            case rulesPage:
                rulesMenu.update(deltaTime);
                break;
        }
    }

    @Override
    public void draw() {
        switch (getStatus()) {
            case mainMenu: {
                mainMenu.draw();
                break;
            }
            case levelSelection: {
                levelSelectMenu.draw();
                break;
            }
            case rulesPage:
                rulesMenu.draw();
                break;
            case connecting:
                mainMenu.draw();
                break;
        }
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (getStatus()) {
            case mainMenu: {
                mainMenu.keyDown(keycode);
                break;
            }
            case levelSelection: {
                levelSelectMenu.keyDown(keycode);
                break;
            }
            case rulesPage: {
                rulesMenu.keyDown(keycode);
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 coordinates = getViewport().unproject(new Vector3(screenX, screenY, 0));

        switch (getStatus()) {
            case mainMenu: {
                mainMenu.click(coordinates.x, coordinates.y);
                break;
            }
            case levelSelection: {
                levelSelectMenu.click(coordinates.x, coordinates.y);
                break;
            }
            case rulesPage:
                rulesMenu.click(coordinates.x, coordinates.y);
                break;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    protected void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void prepare() {

    }

    @Override
    protected void loadData() {
        loadAsset("menu.atlas", TextureAtlas.class);
        loadAsset("yellow_pop.wav", Sound.class);
        loadAsset("oneup.wav", Sound.class);
    }

    @Override
    protected void show() {
        rulesMenu = new RulesMenu(this);
        show(MenuRoomStatus.mainMenu);
    }

    @Override
    protected void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void resume() {
        // TODO Auto-generated method stub

    }


}

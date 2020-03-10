package io.github.deianvn.gameutils.scene;

import com.badlogic.gdx.InputProcessor;

public class EmptyLoadingRoom extends LoadingRoom {

    public EmptyLoadingRoom(Game game) {
        super(game);
    }

    @Override
    public void draw() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void prepare() {

    }

    @Override
    protected void resize(int width, int height) {

    }

    @Override
    protected void show() {

    }

    @Override
    protected void pause() {

    }

    @Override
    protected void resume() {

    }

    @Override
    protected InputProcessor getInputProcessor() {
        return null;
    }

    @Override
    protected void computeDimensions() {

    }
}

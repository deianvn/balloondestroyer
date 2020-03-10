package io.github.deianvn.gameutils.scene;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class StageRoom extends Room {

    private Stage stage;

    public StageRoom(Game game) {
        super(game);
        stage = new Stage(game.getViewport());
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    protected InputProcessor getInputProcessor() {
        return stage;
    }
}

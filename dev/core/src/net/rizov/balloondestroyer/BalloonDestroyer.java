package net.rizov.balloondestroyer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.rizov.balloondestroyer.room.menu.MenuRoom;
import net.rizov.gameutils.scene.Game;

public class BalloonDestroyer extends ApplicationAdapter {

	private static final int SCREEN_WIDTH = 480;

	private static final int SCREEN_HEIGHT = 800;

	private Game game;

	public BalloonDestroyer(Game game) {
		this.game = game;
	}

	@Override
	public void create () {
		Gdx.input.setCatchBackKey(true);
		OrthographicCamera camera = new OrthographicCamera();
		camera.position.set(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 0);
		Viewport viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
		game.setViewport(viewport);
		game.setCurrentRoom(new MenuRoom(game));
	}

	@Override
	public void render () {
		super.render();
		game.process();
	}

	@Override
	public void pause() {
		super.pause();
		game.pause();
	}

	@Override
	public void resume() {
		super.resume();
		game.resume();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		game.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		game.dispose();
	}

}

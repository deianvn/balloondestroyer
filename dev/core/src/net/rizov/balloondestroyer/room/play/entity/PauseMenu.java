package net.rizov.balloondestroyer.room.play.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import net.rizov.balloondestroyer.utils.config.ConfigManager;
import net.rizov.balloondestroyer.room.play.PlayRoomEvent;
import net.rizov.gameutils.scene.RoomEntity;
import net.rizov.gameutils.scene.TextureEntity;

public class PauseMenu extends TextureEntity {
	
	private Circle resumeArea = new Circle(new Vector2(113, 49), 35f);
	
	private Circle restartArea = new Circle(new Vector2(214, 49), 35f);
	
	private Circle quitArea = new Circle(new Vector2(313, 49), 35f);
	
	private Sound tapSound;
	
	public PauseMenu(RoomEntity parent) {
		super(parent);

		TextureAtlas playAtlas = getRoom().getAsset("play.atlas", TextureAtlas.class);
		tapSound = getRoom().getAsset("yellow_pop.wav", Sound.class);
		setTextureRegion(playAtlas.findRegion("dialog-paused"));
	}
	
	@Override
	public void update(float deltaTime) {
		
	}
	
	public PlayRoomEvent getItem(float x, float y) {
		x -= getPositionX();
		y -= getPositionY();
		
		if (resumeArea.contains(x, y)) {
			onClick();
			return PlayRoomEvent.resume;
		} else if (restartArea.contains(x, y)) {
			onClick();
			return PlayRoomEvent.restart;
		} else if (quitArea.contains(x, y)) {
			onClick();
			return PlayRoomEvent.quit;
		}
		
		return null;
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

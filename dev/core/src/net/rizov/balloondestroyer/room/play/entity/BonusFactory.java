package net.rizov.balloondestroyer.room.play.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.rizov.gameutils.scene.RoomEntity;

public class BonusFactory {
	
	private TextureRegion x2Region;
	
	private TextureRegion x3Region;
	
	public BonusFactory(TextureAtlas playAtlas) {
		x2Region = playAtlas.findRegion("bonus-x2");
		x3Region = playAtlas.findRegion("bonus-x3");
	}
	
	public Bonus createBonus(RoomEntity parent, BurstType type, float x, float y) {
		switch (type) {
		case two :
			return new Bonus(parent, x2Region, x, y);
		case three :
			return new Bonus(parent, x3Region, x, y);
		default :
		}
		
		return null;
	}
	
}

package net.rizov.balloondestroyer.room.play.entity;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import net.rizov.gameutils.scene.RoomEntity;

public class CloudGenerator {

	private TextureAtlas playAtlas;
	
	private Map<Time, CloudFactory[]> factories = new HashMap<Time, CloudFactory[]>();
	
	private interface CloudFactory {
		Cloud createCloud(RoomEntity parent);
	}
	
	public CloudGenerator(RoomEntity parent) {
		playAtlas = parent.getRoom().getAsset("play.atlas", TextureAtlas.class);
		setupFactories();
	}
	
	private void setupFactories() {

		factories.put(Time.day, new CloudFactory[] {
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("day-cloud1"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("day-cloud2"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("day-cloud3"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("day-cloud4"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("day-cloud5"));
					}
				},
		});
		
		factories.put(Time.dusk, new CloudFactory[] {
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("dusk-cloud1"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("dusk-cloud2"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("dusk-cloud3"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("dusk-cloud4"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("dusk-cloud5"));
					}
				},
		});
		
		factories.put(Time.night, new CloudFactory[] {
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("night-cloud1"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("night-cloud2"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("night-cloud3"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("night-cloud4"));
					}
				},
				
				new CloudFactory() {
					
					@Override
					public Cloud createCloud(RoomEntity parent) {
						return new Cloud(parent, playAtlas.findRegion("night-cloud5"));
					}
				},
		});
	}
	
	public Cloud generateCloud(RoomEntity parent, Time type) {
		int index = (int)(Math.random() * 5);
		Cloud cloud = factories.get(type)[index].createCloud(parent);
		float speed = ((float)Math.random() - 0.5f) * 100.0f;
		cloud.setSpeed(speed);
		cloud.setPositionY((float)Math.random() * (800 - cloud.getTextureRegion().getRegionHeight()));
		cloud.clear();
		
		return cloud;
	}
	
}

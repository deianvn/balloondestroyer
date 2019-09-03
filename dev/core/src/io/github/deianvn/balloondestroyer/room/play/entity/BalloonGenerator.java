package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import io.github.deianvn.balloondestroyer.room.entity.Balloon;
import net.rizov.gameutils.scene.Room;
import net.rizov.gameutils.scene.RoomEntity;

public class BalloonGenerator {

    private static final float LEVEL_FACTOR = 0.995f;

    public enum BalloonType {
        red(0),
        blue(1),
        yellow(2),
        green(3);

        private final int value;

        BalloonType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    interface BalloonFactory {
        Balloon createBalloon(RoomEntity parent);
    }

    private BalloonFactory tntBalloonFactory = new BalloonFactory() {
        @Override
        public Balloon createBalloon(RoomEntity parent) {
            Balloon balloon = new Balloon(parent, playAtlas.findRegion("balloon-tnt"), playAtlas.findRegion("balloon-burst-tnt"), sounds[3], new Ellipse(
                    new Vector2(0, 0),
                    54,
                    80
            ));

            balloon.setPoints(0);
            balloon.setSpeed(400);
            balloon.setDiversion(40);
            balloon.clear();

            return balloon;
        }
    };

    private BalloonFactory[] factories = new BalloonFactory[]{

            new BalloonFactory() {
                @Override
                public Balloon createBalloon(RoomEntity parent) {
                    Balloon balloon = new Balloon(parent, playAtlas.findRegion("balloon-red"), playAtlas.findRegion("balloon-burst-red"), sounds[0], new Ellipse(
                            new Vector2(0, 0),
                            98,
                            128
                    ));

                    balloon.setPoints(5);
                    balloon.setSpeed(140);
                    balloon.setDiversion(15);
                    balloon.clear();

                    return balloon;
                }
            },

            new BalloonFactory() {
                @Override
                public Balloon createBalloon(RoomEntity parent) {
                    Balloon balloon = new Balloon(parent, playAtlas.findRegion("balloon-blue"), playAtlas.findRegion("balloon-burst-blue"), sounds[1], new Ellipse(
                            new Vector2(0, 0),
                            79,
                            130
                    ));

                    balloon.setPoints(10);
                    balloon.setSpeed(180);
                    balloon.setDiversion(20);
                    balloon.clear();

                    return balloon;
                }
            },

            new BalloonFactory() {
                @Override
                public Balloon createBalloon(RoomEntity parent) {
                    Balloon balloon = new Balloon(parent, playAtlas.findRegion("balloon-yellow"), playAtlas.findRegion("balloon-burst-yellow"), sounds[2], new Ellipse(
                            new Vector2(0, 0),
                            73,
                            107
                    ));

                    balloon.setPoints(20);
                    balloon.setSpeed(220);
                    balloon.setDiversion(25);
                    balloon.clear();

                    return balloon;
                }
            },

            new BalloonFactory() {
                @Override
                public Balloon createBalloon(RoomEntity parent) {
                    Balloon balloon = new Balloon(parent, playAtlas.findRegion("balloon-green"), playAtlas.findRegion("balloon-burst-green"), sounds[3], new Ellipse(
                            new Vector2(0, 0),
                            54,
                            80
                    ));

                    balloon.setPoints(50);
                    balloon.setSpeed(300);
                    balloon.setDiversion(30);
                    balloon.clear();

                    return balloon;
                }
            },
    };

    private float[] balloonRandomness;

    private TextureAtlas playAtlas;

    private Sound[] sounds;

    public BalloonGenerator(RoomEntity parent) {
        Room<?, ?> room = parent.getRoom();
        this.playAtlas = room.getAsset("play.atlas", TextureAtlas.class);

        sounds = new Sound[]{
                room.getAsset("red_pop.wav", Sound.class),
                room.getAsset("blue_pop.wav", Sound.class),
                room.getAsset("yellow_pop.wav", Sound.class),
                room.getAsset("green_pop.wav", Sound.class)
        };

        clear();
    }

    public void incrementLevel() {
        for (int i = 0; i < balloonRandomness.length; i++) {
            balloonRandomness[i] *= LEVEL_FACTOR;
        }
    }

    public Balloon generate(Room<?, ?> room) {

        float r = (float) Math.random();
        int i = 0;

        for (; i < balloonRandomness.length; i++) {
            if (r < balloonRandomness[i]) {
                break;
            }
        }

        return factories[i].createBalloon(room);

    }

    public Balloon createBalloon(Room<?, ?> room, BalloonType type) {
        return factories[type.getValue()].createBalloon(room);
    }

    public Balloon createTntBalloon(Room<?, ?> room) {
        return tntBalloonFactory.createBalloon(room);
    }

    public void clear() {
        balloonRandomness = new float[]{0.6f, 0.8f, 0.95f};
    }

}

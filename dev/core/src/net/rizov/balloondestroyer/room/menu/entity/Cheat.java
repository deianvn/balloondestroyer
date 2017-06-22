package net.rizov.balloondestroyer.room.menu.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import net.rizov.gameutils.scene.Room;
import net.rizov.gameutils.scene.Updatable;

public class Cheat implements Updatable {

    private static final float DELAY = 1.5f;

    private Rectangle topLeft = new Rectangle(0, 700, 100, 100);

    private Rectangle topRight = new Rectangle(380, 700, 100, 100);

    private Rectangle bottomRight = new Rectangle(380, 0, 100, 100);

    private Rectangle[] pattern = new Rectangle[]{
            topLeft, bottomRight, topLeft, bottomRight, topRight, bottomRight, topRight, bottomRight, bottomRight
    };

    private boolean cheatEnabled = false;

    private int index = 0;

    private float timeToFailure = DELAY;

    private int skipWave = 0;

    private Sound notifySound;

    public Cheat(Room<?, ?> room) {
        notifySound = room.getAsset("oneup.wav", Sound.class);
    }

    @Override
    public void update(float deltaTime) {
        if (index > 0) {
            timeToFailure -= deltaTime;
        }
    }

    public void click(float x, float y) {
        if (cheatEnabled) {
            if (y >= 700 && skipWave < 99) {
                skipWave++;
            }

            return;
        }

        if (timeToFailure < 0) {
            timeToFailure = DELAY;
            index = 0;
        }

        if (pattern[index].contains(x, y)) {
            index++;
            timeToFailure = DELAY;

            if (index >= pattern.length) {
                cheatEnabled = true;

                notifySound.play(1.0f);
            }
        }
    }

    public boolean isCheatEnabled() {
        return cheatEnabled;
    }

    public int getSkipWaves() {
        return skipWave;
    }

}

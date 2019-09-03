package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import io.github.deianvn.balloondestroyer.room.entity.Balloon;
import io.github.deianvn.balloondestroyer.room.play.entity.BalloonGenerator.BalloonType;
import io.github.deianvn.balloondestroyer.utils.config.ConfigManager;
import net.rizov.gameutils.scene.RoomEntity;

public class Wave extends RoomEntity {

    final static float LEVEL_FREQUENCY_FACTOR = 0.99f;

    private boolean running;

    private int number;

    private int hardness;

    private int wave;

    private float frequency;

    private float timeToNextBalloons;

    private int escapedBalloons;

    private BalloonGenerator generator;

    private WaveTitle waveTitle;

    private Sound escapeSound;

    private boolean showTitle;

    private Array<Balloon> balloons;

    private CloudGroup cloudGroup;

    private boolean finalLevel;

    private float finalLevelPause = 7;

    public Wave(RoomEntity parent, Time time) {
        super(parent);

        cloudGroup = new CloudGroup(getRoom());
        cloudGroup.setNumber(3);
        cloudGroup.setTime(time);

        frequency = 1.0f;
        timeToNextBalloons = frequency;
        wave = 1;
        escapeSound = getRoom().getAsset("escape.wav", Sound.class);
        balloons = new Array<Balloon>();
        generator = new BalloonGenerator(getRoom());
        waveTitle = new WaveTitle(getRoom());
        running = true;
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void update(float deltaTime) {
        if (running) {
            cloudGroup.update(deltaTime);

            escapedBalloons = 0;

            if (showTitle) {
                waveTitle.update(deltaTime);

                if (!waveTitle.isAvailable()) {
                    showTitle = false;
                }

                return;
            }

            int i = 0;

            for (Balloon balloon : balloons) {
                balloon.update(deltaTime);

                if (!balloon.isAvailable()) {
                    if (!balloon.isBurst()) {
                        escapedBalloons++;

                        if (ConfigManager.isVibrationEnabled()) {
                            Gdx.input.vibrate(700);
                        }

                        if (ConfigManager.isSoundEnabled()) {
                            escapeSound.play(1.0f);
                        }
                    }

                    balloons.removeIndex(i);
                }

                i++;
            }

            if (number > 0) {
                timeToNextBalloons -= deltaTime;

                if (timeToNextBalloons < 0) {
                    if (finalLevel) {
                        BalloonType type;

                        if (number > hardness * 50 + hardness * 20 + hardness * 10) {
                            type = BalloonType.red;
                        } else if (number > hardness * 50 + hardness * 20) {
                            type = BalloonType.blue;
                        } else if (number > hardness * 50) {
                            type = BalloonType.yellow;
                        } else {
                            type = BalloonType.green;
                        }

                        addBalloon(type);
                        number--;
                    } else {
                        for (i = 0; i < hardness; i++) {
                            addBalloon();
                            number--;
                        }
                    }

                    timeToNextBalloons = frequency;
                }
            } else if (finalLevel) {
                finalLevelPause -= deltaTime;
            }
        }
    }

    @Override
    public void draw() {
        if (showTitle) {
            cloudGroup.draw();
            waveTitle.draw();
            return;
        }

        for (Balloon balloon : balloons) {
            balloon.draw();
        }

        cloudGroup.draw();
    }

    public int escapedBalloons() {
        return escapedBalloons;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
        cloudGroup.setNumber(hardness);
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int number) {
        waveTitle.setWave(number);
        balloons.clear();
        generator.clear();
        frequency = 1.0f;

        for (wave = 1; wave < number; wave++) {
            incrementLevel();
        }

        showTitle = true;

        if (wave == 100) {
            finalLevel = true;
            finalLevelPause = 7;
        } else {
            finalLevel = false;
        }
    }

    public void nextWave() {
        wave++;
        incrementLevel();
        showTitle = true;
    }

    private void incrementLevel() {
        generator.incrementLevel();
        frequency *= LEVEL_FREQUENCY_FACTOR;
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    public Burst burst(float x, float y) {
        int count = 0;
        int score = 0;

        for (Balloon balloon : balloons) {
            if (!balloon.isBurst() && balloon.contains(x, y)) {
                balloon.burst();
                score += balloon.getPoints();
                count++;
            }
        }

        if (count == 1) {
            return new Burst(BurstType.one, score);
        } else if (count == 2) {
            return new Burst(BurstType.two, score * 2);
        } else if (count >= 3) {
            return new Burst(BurstType.three, score * 3);
        }

        return null;
    }

    public boolean isFinished() {
        return number == 0 && balloons.size == 0 && (!finalLevel || finalLevelPause < 0);
    }

    public void setTime(Time time) {
        cloudGroup.setTime(time);
    }

    private void addBalloon() {
        balloons.add(generator.generate(getRoom()));
    }

    private void addBalloon(BalloonType type) {
        balloons.add(generator.createBalloon(getRoom(), type));
    }

}

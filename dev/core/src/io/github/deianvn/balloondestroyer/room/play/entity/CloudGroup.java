package io.github.deianvn.balloondestroyer.room.play.entity;

import com.badlogic.gdx.utils.Array;
import io.github.deianvn.gameutils.scene.RoomEntity;

public class CloudGroup extends RoomEntity {

    private CloudGenerator generator;

    private Array<Cloud> clouds;

    private int number;

    private Time time;

    public CloudGroup(RoomEntity parent) {
        super(parent);
        generator = new CloudGenerator(parent);
        clouds = new Array<Cloud>();
    }

    @Override
    protected void computeDimensions() {

    }

    @Override
    public void update(float deltaTime) {
        if (clouds.size < number) {
            for (int i = clouds.size; i < number; i++) {
                clouds.add(generator.generateCloud(getParent(), time));
            }
        }

        int i = 0;

        for (Cloud cloud : clouds) {
            cloud.update(deltaTime);

            if (!cloud.isAvailable()) {
                clouds.removeIndex(i);
            }

            i++;
        }
    }

    @Override
    public void draw() {
        for (Cloud cloud : clouds) {
            cloud.draw();
        }
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setTime(Time time) {
        if (this.time == time) {
            return;
        }

        this.time = time;
        clouds.clear();
    }

    public Time getTime() {
        return time;
    }

}

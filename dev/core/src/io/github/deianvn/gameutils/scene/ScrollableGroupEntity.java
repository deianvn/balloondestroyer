package io.github.deianvn.gameutils.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

public abstract class ScrollableGroupEntity extends GroupEntity {

    private Rectangle visibleBounds;

    private Camera camera;

    private float min;

    private float max;

    public ScrollableGroupEntity(Room<?, ?> room) {
        super(room);
        this.camera = room.getCamera();

        visibleBounds = new Rectangle(camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight);
    }

    public ScrollableGroupEntity(RoomEntity parent) {
        super(parent);
        this.camera = parent.getRoom().getCamera();

        visibleBounds = new Rectangle(camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight);
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMin() {
        return min;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMax() {
        return max;
    }

    public float getRange() {
        return Math.abs(getMax() - getMin());
    }

    public void scrollTo(float position) {
        if (position > getMax()) {
            position = getMax();
        }

        if (position < getMin()) {
            position = getMin();
        }

        setPositionY(position);
    }

    public void setScroll(float scroll) {
        if (scroll > 0f) {
            scroll = 0f;
        }

        if (scroll > 1f) {
            scroll = 1f;
        }

        scrollTo(getMin() + getRange() * scroll);
    }

    @Override
    public void draw() {
        SpriteBatch sb = getSpriteBatch();
        drawBegin(sb);
        super.draw();
        drawEnd(sb);
    }

    protected void drawBegin(SpriteBatch sb) {
        Rectangle scissors = new Rectangle();
        ScissorStack.calculateScissors(camera, sb.getTransformMatrix(), visibleBounds, scissors);
        ScissorStack.pushScissors(scissors);
    }

    protected void drawEnd(SpriteBatch sb) {
        sb.flush();
        ScissorStack.popScissors();
    }

}

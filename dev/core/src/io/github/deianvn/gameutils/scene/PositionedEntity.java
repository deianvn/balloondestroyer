package io.github.deianvn.gameutils.scene;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class PositionedEntity implements Entity {

    private Vector2 position;

    private Vector2 dimensions;

    private float originX = 0f;

    private float originY = 0f;

    private float rotation = 0f;

    private float scaleX = 1f;

    private float scaleY = 1f;

    private Array<PositionedEntityModifier> modifiers = new Array<PositionedEntityModifier>();

    private Map<PositionedEntityModifier, Runnable> modifierActions = new HashMap<PositionedEntityModifier, Runnable>();

    public PositionedEntity() {
        position = new Vector2(0, 0);
        dimensions = new Vector2(0, 0);
    }

    @Override
    public void update(float deltaTime) {
        if (modifiers.size > 0) {
            Iterator<PositionedEntityModifier> modifiersIterator = modifiers.iterator();

            while (modifiersIterator.hasNext()) {
                PositionedEntityModifier modifier = modifiersIterator.next();
                modifier.update(deltaTime);

                if (modifier.isFinished()) {
                    Runnable modifierAction = modifierActions.get(modifier);
                    modifiersIterator.remove();

                    if (modifierAction != null) {
                        modifierAction.run();
                    }
                }
            }
        }
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        onMove();
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getPositionX() {
        return position.x;
    }

    public void setPositionX(float x) {
        position.x = x;
        onMove();
    }

    public float getPositionY() {
        return position.y;
    }

    public void setPositionY(float y) {
        position.y = y;
        onMove();
    }

    public float getWidth() {
        return dimensions.x;
    }

    public float getHeight() {
        return dimensions.y;
    }

    public void setWidth(float width) {
        dimensions.x = width;
    }

    public void setHeight(float height) {
        dimensions.y = height;
    }

    public void setBounds(Rectangle bounds) {
        setPositionX(bounds.x);
        setPositionY(bounds.y);
        setWidth(bounds.width);
        setHeight(bounds.height);
    }

    public Rectangle getBounds() {
        return new Rectangle(getPositionX(), getPositionY(), getWidth(), getHeight());
    }

    protected abstract void computeDimensions();

    public void shiftPositionX(float amount) {
        setPositionX(getPositionX() + amount);
    }

    public void shiftPositionY(float amount) {
        setPositionY(getPositionY() + amount);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public void clearRotation() {
        rotation = 0f;
    }

    public void setOrigin(float x, float y) {
        this.originX = x;
        this.originY = y;
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void addModifier(PositionedEntityModifier modifier, Runnable onFinished) {
        addModifier(modifier);
        modifierActions.put(modifier, onFinished);
    }

    public void addModifier(PositionedEntityModifier modifier) {
        modifiers.add(modifier);
        modifier.setEntity(this);
    }

    public boolean modifiersFinished() {
        for (PositionedEntityModifier modifier : modifiers) {
            if (!modifier.isFinished()) {
                return false;
            }
        }

        return true;
    }

    public boolean modifiersFinished(Class<PositionedEntityModifier> type) {
        for (PositionedEntityModifier modifier : modifiers) {
            if (modifier.getClass().isAssignableFrom(type) && !modifier.isFinished()) {
                return false;
            }
        }

        return true;
    }

    protected void onMove() {

    }

}

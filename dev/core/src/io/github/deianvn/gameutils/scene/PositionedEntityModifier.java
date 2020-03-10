package io.github.deianvn.gameutils.scene;

public abstract class PositionedEntityModifier implements Updatable {

    private PositionedEntity entity;

    void setEntity(PositionedEntity entity) {
        this.entity = entity;
    }

    protected PositionedEntity getEntity() {
        return entity;
    }

    public abstract boolean isFinished();

}

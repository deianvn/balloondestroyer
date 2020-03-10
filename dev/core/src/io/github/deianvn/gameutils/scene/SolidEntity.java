package io.github.deianvn.gameutils.scene;

public abstract class SolidEntity extends RoomEntity {

    private CollisionStrategy collisionStrategy;

    public SolidEntity(RoomEntity parent) {
        super(parent);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public void setCollisionStrategy(CollisionStrategy collisionStrategy) {
        this.collisionStrategy = collisionStrategy;
    }

    CollisionStrategy getCollisionStrategy() {
        return collisionStrategy;
    }

    public boolean detectCollision(SolidEntity entity) {
        return getBounds().overlaps(entity.getBounds());
    }

    public boolean detectFineCollision(SolidEntity entity) {
        if (collisionStrategy.isSupported(entity.getCollisionStrategy().getClass())) {
            return collisionStrategy.detectCollision(entity.getCollisionStrategy());
        }

        return false;
    }

}

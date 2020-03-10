package io.github.deianvn.gameutils.scene;

public abstract class LoadingRoom extends Room<LoadingRoomStatus, LoadingRoomAction> {

    private Room<?, ?> room;

    public LoadingRoom(Game game) {
        super(game);
    }

    public void setRoom(Room<?, ?> room) {
        this.room = room;
    }

    @Override
    public void update(float deltaTime) {
        if (room.ready()) {
            getGame().forceCurrentRoom(room);
        }
    }

    @Override
    protected void hide() {

    }

    @Override
    protected void dispose() {

    }

}

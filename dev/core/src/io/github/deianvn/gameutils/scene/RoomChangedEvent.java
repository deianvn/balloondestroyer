package io.github.deianvn.gameutils.scene;

public class RoomChangedEvent {

    private Room<?, ?> room;

    public RoomChangedEvent(Room<?, ?> room) {
        super();
        this.room = room;
    }

    public Room<?, ?> getRoom() {
        return room;
    }

}

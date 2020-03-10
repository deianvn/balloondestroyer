package io.github.deianvn.gameutils.scene;

public class StatusChangedEvent<S> {

    private S status;

    private S oldStatus;

    public StatusChangedEvent(S status, S oldStatus) {
        super();
        this.status = status;
        this.oldStatus = oldStatus;
    }

    public S getStatus() {
        return status;
    }

    public S getPreviousStatus() {
        return oldStatus;
    }

}

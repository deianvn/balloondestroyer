package io.github.deianvn.gameutils.scene;

public interface StatusChangeListener<S> {

    public void statusChanged(StatusChangedEvent<S> event);

}

package io.github.deianvn.gameutils.scene;

import java.util.Iterator;
import java.util.Vector;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.async.ThreadUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Room<S, E> extends RoomEntity {

    private Game game;

    private S status;

    private volatile boolean prepared;

    private Vector2 position = new Vector2(0, 0);

    private Vector<StatusChangeListener<S>> statusChangeListeners = new Vector<StatusChangeListener<S>>();

    private Vector<EventListener<E>> eventListeners = new Vector<EventListener<E>>();

    public Room(Game game) {
        super(null);
        this.game = game;

        new Thread(new Runnable() {
            @Override
            public void run() {
                prepare();
                prepared = true;
            }
        }).run();
    }

    @Override
    public Room<?, ?> getRoom() {
        return this;
    }

    @Override
    public void setParent(RoomEntity parent) {

    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getPositionX() {
        return 0;
    }

    @Override
    public float getPositionY() {
        return 0;
    }

    @Override
    public float getWidth() {
        return game.getViewport().getWorldWidth();
    }

    @Override
    public float getHeight() {
        return game.getViewport().getWorldHeight();
    }

    public Viewport getViewport() {
        return game.getViewport();
    }

    public Camera getCamera() {
        return game.getCamera();
    }

    public Game getGame() {
        return game;
    }

    public void setStatus(S status) {
        if (this.status == status) {
            return;
        }

        S oldStatus = this.status;
        this.status = status;

        if (statusChangeListeners.size() > 0) {
            for (StatusChangeListener<S> listener : statusChangeListeners) {
                StatusChangedEvent<S> event = new StatusChangedEvent<S>(status, oldStatus);
                listener.statusChanged(event);
            }
        }
    }

    public S getStatus() {
        return status;
    }

    public void addStatusChangeListener(StatusChangeListener<S> listener) {
        statusChangeListeners.add(listener);
    }

    public void removeStatusChangeListener(StatusChangeListener<S> listener) {
        statusChangeListeners.remove(listener);
    }

    public Iterator<StatusChangeListener<S>> getStatusChnageListenersIterator() {
        return statusChangeListeners.iterator();
    }

    public void addEventListener(EventListener<E> listener) {
        eventListeners.add(listener);
    }

    public void removeEventListener(EventListener<E> listener) {
        eventListeners.remove(listener);
    }

    public Iterator<EventListener<E>> getEventListenersIterator() {
        return eventListeners.iterator();
    }

    public void triggerEvent(E event) {
        if (eventListeners.size() > 0) {
            for (EventListener<E> listener : eventListeners) {
                listener.eventHappened(event);
            }
        }
    }

    public <T> T getAsset(String fileName, Class<T> type) {
        return game.getAssetManager().get(fileName, type);
    }

    public void disposeAsset(String fileName) {
        game.getAssetManager().unload(fileName);
    }

    public boolean ready() {
        return game.getAssetManager().update() && prepared;
    }

    public void finishLoading() {
        game.getAssetManager().finishLoading();

        while (!prepared) {
            ThreadUtils.yield();
        }
    }

    protected <T> void loadAsset(String fileName, Class<T> type) {
        game.getAssetManager().load(fileName, type);
    }

    protected abstract InputProcessor getInputProcessor();

    protected abstract void loadData();

    protected abstract void prepare();

    protected abstract void resize(int width, int height);

    protected abstract void show();

    protected abstract void hide();

    protected abstract void pause();

    protected abstract void resume();

    protected void dispose() {
        game.getAssetManager().clear();
    }

}

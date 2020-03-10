package io.github.deianvn.gameutils.scene;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Game {

    private Viewport viewport;

    private SpriteBatch spriteBatch;

    private DecalBatch decalBatch;

    private Room<?, ?> currentRoom;

    private LoadingRoom loadingRoom;

    private AssetManager assetManager;

    private Map<Class<?>, Factory> factories = new HashMap<Class<?>, Factory>();

    private Vector<RoomChangeListener> roomChangeListeners = new Vector<RoomChangeListener>();

    public Game() {

    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
        assetManager = new AssetManager();
        enableSpriteBatch();
    }

    public void enableSpriteBatch() {
        if (spriteBatch == null) {
            spriteBatch = new SpriteBatch();
        }
    }

    public void disableSpriteBatch() {
        if (spriteBatch != null) {
            spriteBatch.dispose();
            spriteBatch = null;
        }
    }

    public boolean isSpriteBatchEnabled() {
        return spriteBatch != null;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void enableDecalBatch() {
        if (decalBatch == null) {
            decalBatch = new DecalBatch(new CameraGroupStrategy(getCamera()));
        }
    }

    public void disableDecalBatch() {
        if (decalBatch != null) {
            decalBatch.dispose();
            decalBatch = null;
        }
    }

    public boolean isDecalBatchEnabled() {
        return decalBatch != null;
    }

    public DecalBatch getDecalBatch() {
        return decalBatch;
    }

    public <T> void addFactory(Class<T> type, Factory<T> implementationFactory) {
        factories.put(type, implementationFactory);
    }

    public <T> T inject(Class<T> type) {
        return type.cast(factories.get(type).create());
    }

    public void forceCurrentRoom(Room<?, ?> room) {
        currentRoom = room;

        if (roomChangeListeners.size() > 0) {
            for (RoomChangeListener listener : roomChangeListeners) {
                listener.roomChanged(new RoomChangedEvent(currentRoom));
            }
        }

        if (currentRoom != null) {
            InputProcessor inputProcessor = currentRoom.getInputProcessor();

            if (inputProcessor != null) {
                Gdx.input.setInputProcessor(inputProcessor);
            }

            currentRoom.show();
        }
    }

    public void setCurrentRoom(Room<?, ?> room) {
        getCamera().lookAt(getViewport().getWorldWidth() / 2, getViewport().getWorldHeight() / 2, 0);
        getCamera().update();

        if (currentRoom != null) {
            currentRoom.hide();
            currentRoom.dispose();
        }

        if (loadingRoom != null) {
            loadingRoom.setRoom(room);
            room.loadData();
            room.prepare();
            room = loadingRoom;
        } else {
            room.loadData();
            room.prepare();
            room.finishLoading();
        }

        forceCurrentRoom(room);
    }

    public void setLoadingRoom(LoadingRoom room) {
        if (loadingRoom != null) {
            loadingRoom.hide();
            loadingRoom.dispose();
        }

        loadingRoom = room;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Camera getCamera() {
        return viewport.getCamera();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void addRoomChangeListener(RoomChangeListener listener) {
        roomChangeListeners.addElement(listener);
    }

    public void removeRoomChangeListener(RoomChangeListener listener) {
        roomChangeListeners.remove(listener);
    }

    public Iterator<RoomChangeListener> getRoomChangeListenersIterator() {
        return roomChangeListeners.iterator();
    }

    public void process() {
        float dt = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (currentRoom != null) {
            currentRoom.update(dt);

            if (spriteBatch != null) {
                spriteBatch.begin();
                getCamera().update();
                spriteBatch.setProjectionMatrix(getCamera().combined);
            }

            currentRoom.draw();

            if (spriteBatch != null) {
                spriteBatch.end();
            }

            if (decalBatch != null) {
                decalBatch.flush();
            }
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);

        if (currentRoom != null) {
            currentRoom.resize(width, height);
        }
    }

    public void pause() {
        if (currentRoom != null) {
            currentRoom.pause();
        }
    }

    public void resume() {
        if (currentRoom != null) {
            currentRoom.resume();
        }
    }

    public void dispose() {
        assetManager.dispose();
    }

    public void quit() {
        if (currentRoom != null) {
            currentRoom.hide();
        }

        Gdx.app.exit();

    }

}

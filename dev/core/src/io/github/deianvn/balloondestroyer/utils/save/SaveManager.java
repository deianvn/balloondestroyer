package io.github.deianvn.balloondestroyer.utils.save;

import net.rizov.gameutils.scene.Game;

public abstract class SaveManager {

    private static SaveConnector connector;

    protected static SaveData saveData;

    public static void create(Game game) {
        connector = game.inject(SaveConnector.class);
    }

    public static void load() {
        saveData = connector.load();
    }

    public static void save() {
        if (saveData != null) {
            connector.save(saveData);
        }
    }

    public static boolean hasSaveData() {
        return connector.hasSaveData();
    }

}

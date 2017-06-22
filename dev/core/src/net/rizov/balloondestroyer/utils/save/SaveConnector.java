package net.rizov.balloondestroyer.utils.save;

public interface SaveConnector {

    void save(SaveData saveData);

    SaveData load();

    boolean hasSaveData();

}

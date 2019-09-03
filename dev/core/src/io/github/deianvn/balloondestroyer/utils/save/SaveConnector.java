package io.github.deianvn.balloondestroyer.utils.save;

public interface SaveConnector {

    void save(SaveData saveData);

    SaveData load();

    boolean hasSaveData();

}

package net.rizov.balloondestroyer.utils.save;

public class BalloonDestroyerSaveManager extends SaveManager {

    public static int getWave(int hardness) {
        WaveSaveData waveSaveData = getSaveData().getWave(hardness);
        return waveSaveData == null ? 0 : waveSaveData.getWave();
    }

    public static void setWave(int wave, int hardness) {
        getSaveData().setWave(new WaveSaveData(wave, hardness));
    }

    public static int getHighScore() {
        return getSaveData().getHighScore();
    }

    public static void setHighScore(int highScore) {
        getSaveData().setHighScore(highScore);
    }

    private static BalloonDestroyerSaveData getSaveData() {
        return (BalloonDestroyerSaveData) saveData;
    }

}

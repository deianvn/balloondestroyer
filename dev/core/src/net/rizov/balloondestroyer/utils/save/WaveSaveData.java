package net.rizov.balloondestroyer.utils.save;

public class WaveSaveData {

    private int wave;

    private int hardness;

    public WaveSaveData(int wave, int hardness) {
        this.wave = wave;
        this.hardness = hardness;
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

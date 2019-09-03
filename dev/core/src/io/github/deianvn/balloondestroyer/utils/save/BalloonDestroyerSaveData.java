package io.github.deianvn.balloondestroyer.utils.save;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class BalloonDestroyerSaveData implements SaveData {

    private static final String WAVE_KEY = "wave";

    private static final String HIGH_SCORE_KEY = "highscore";

    private Map<Integer, WaveSaveData> waveSaveDataMap = new HashMap<Integer, WaveSaveData>();

    private int highScore;

    public byte[] getData() {
        StringBuilder sb = new StringBuilder();

        for (Integer key : waveSaveDataMap.keySet()) {
            WaveSaveData waveSaveData = waveSaveDataMap.get(key);

            if (waveSaveData != null) {
                sb.append(WAVE_KEY + '.' + waveSaveData.getHardness() + "=" + waveSaveData.getWave() + "\n");
            }
        }

        if (highScore >= 0) {
            sb.append(HIGH_SCORE_KEY + "=" + highScore + "\n");
        }

        return sb.toString().getBytes();
    }

    public void setData(byte[] data) {
        waveSaveDataMap.clear();
        highScore = 0;
        String strData = new String(data);
        BufferedReader in = new BufferedReader(new StringReader(strData));
        String line;

        try {
            while ((line = in.readLine()) != null) {
                if (line.startsWith(WAVE_KEY)) {
                    parseWave(line);
                } else if (line.startsWith(HIGH_SCORE_KEY)) {
                    parseHighScore(line);
                }
            }
        } catch (IOException e) {

        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    public WaveSaveData getWave(int hardness) {
        return waveSaveDataMap.get(hardness);
    }

    public void setWave(WaveSaveData waveSaveData) {
        waveSaveDataMap.put(waveSaveData.getHardness(), waveSaveData);
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    private void parseWave(String line) {
        try {
            String key = line.substring(0, line.indexOf('='));
            int hardness = Integer.parseInt(key.substring(key.indexOf('.') + 1));
            int wave = Integer.parseInt(line.substring(line.indexOf('=') + 1, line.length()));

            setWave(new WaveSaveData(wave, hardness));
        } catch (Exception e) {
        }
    }

    private void parseHighScore(String line) {
        try {
            highScore = Integer.parseInt(line.substring(line.indexOf(HIGH_SCORE_KEY + '=') + HIGH_SCORE_KEY.length() + 1, line.length()));
        } catch (NullPointerException e) {
        }
    }

}

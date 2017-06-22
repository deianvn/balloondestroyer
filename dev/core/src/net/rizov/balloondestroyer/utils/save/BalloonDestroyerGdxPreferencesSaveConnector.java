package net.rizov.balloondestroyer.utils.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.rizov.balloondestroyer.utils.helper.CryptHelper;
import net.rizov.gameutils.scene.Game;

public class BalloonDestroyerGdxPreferencesSaveConnector implements SaveConnector {

    private static final String secretToken = "dfjdskKJHKjgdfsg 8^FD&A&B *F&V*DFYHuybujy7USDFCUsdf7SDFYSDFHDS&*^&*^&*^df*^f891`~~";

    private static final String KEY_VERSION = "version";

    private static final String KEY_SAVE_DATA = "savedata";

    private static final String KEY_CHECKSUM = "checksum";

    private SaveNameProvider saveNameProvider;

    public BalloonDestroyerGdxPreferencesSaveConnector(Game game) {
        saveNameProvider = game.inject(SaveNameProvider.class);
    }

    @Override
    public void save(SaveData saveData) {
        Preferences prefs = Gdx.app.getPreferences(saveNameProvider.getName());
        String value = CryptHelper.base64Encode(saveData.getData());
        String sha1 = CryptHelper.sha1(value + secretToken);
        prefs.putString(KEY_VERSION, "1.11");
        prefs.putString(KEY_SAVE_DATA, value);
        prefs.putString(KEY_CHECKSUM, sha1);
        prefs.flush();
    }

    @Override
    public SaveData load() {
        Preferences prefs = Gdx.app.getPreferences(saveNameProvider.getName());
        String value = prefs.getString(KEY_SAVE_DATA);
        byte[] bytes = null;

        if (value != null) {
            bytes = CryptHelper.base64Decode(value);
        }

        SaveData saveData = new BalloonDestroyerSaveData();

        if (bytes != null) {
            saveData.setData(bytes);
        }

        return saveData;
    }

    @Override
    public boolean hasSaveData() {
        Preferences prefs = Gdx.app.getPreferences(saveNameProvider.getName());

        return prefs.contains(KEY_SAVE_DATA);
    }
}

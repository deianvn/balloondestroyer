package io.github.deianvn.balloondestroyer.utils.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.rizov.gameutils.scene.Game;

public class ConfigManager {

    private static boolean created = false;

    private static final String SOUND_ENABLED = "SoundEnabled";

    private static final String VIBRATION_ENABLED = "VibrationEnabled";

    private static String name;

    private static boolean vibrationEnabled = true;

    private static boolean soundEnabled = true;

    public static void create(Game game) {
        name = game.inject(ConfigNameProvider.class).getName();
        created = true;
    }

    public static boolean isVibrationEnabled() {
        return vibrationEnabled;
    }

    public static void setVibrationEnabled(boolean enabled) {
        vibrationEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public static void load() {
        if (!created) {
            return;
        }

        Preferences prefs = Gdx.app.getPreferences(name);

        if (prefs.contains(SOUND_ENABLED)) {
            soundEnabled = prefs.getBoolean(SOUND_ENABLED);
        }

        if (prefs.contains(VIBRATION_ENABLED)) {
            vibrationEnabled = prefs.getBoolean(VIBRATION_ENABLED);
        }
    }

    public static void save() {
        if (!created) {
            return;
        }

        Preferences prefs = Gdx.app.getPreferences(name);
        prefs.putBoolean(SOUND_ENABLED, soundEnabled);
        prefs.putBoolean(VIBRATION_ENABLED, vibrationEnabled);
        prefs.flush();
    }

}

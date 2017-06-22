package net.rizov.balloondestroyer.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

@Deprecated
public class ConfigManager_Old {

	private static final String NAME = "com.soft-fu.game.balloondestroyer.config";

	private static final String COOKIE_CONSENT = "CookieConsent";

	private static final String SOUND_ENABLED = "SoundEnabled";
	
	private static final String VIBRATION_ENABLED = "VibrationEnabled";
	
	private static final String HIGHSCORE = "Highscore";
	
	private static final String CHECKSUM = "Checksum";

	private static boolean cookieConsent = false;

	private static boolean vibrationEnabled = true;
	
	private static boolean soundEnabled = true;
	
	private static int highScore;
	
	private static final String secretToken = "JKHjdshdfjksdd78645r743&*^&*^y7USDFCUsdf7SDFYSDFHDS&*^&*^&*^df*^f891`~~";

	public static boolean isCookieConsent() {
		return cookieConsent;
	}

	public static void setCookieConsent(boolean consent) {
		cookieConsent = consent;
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
	
	public static void setHighScore(int score) {
		highScore = score;
	}
	
	public static int getHighScore() {
		return highScore;
	}
	
	public static void load() {
		Preferences prefs = Gdx.app.getPreferences(NAME);

		if (prefs.contains(COOKIE_CONSENT)) {
			cookieConsent = prefs.getBoolean(COOKIE_CONSENT);
		}

		if (prefs.contains(SOUND_ENABLED)) {
			soundEnabled = prefs.getBoolean(SOUND_ENABLED);
		}
		
		if (prefs.contains(VIBRATION_ENABLED)) {
			vibrationEnabled = prefs.getBoolean(VIBRATION_ENABLED);
		}
		
		if (prefs.contains(HIGHSCORE) && prefs.contains(CHECKSUM)) {
			int score = prefs.getInteger(HIGHSCORE);
			String checksum = prefs.getString(CHECKSUM);
			
			if (checksum.equals(sha1(secretToken + score))) {
				highScore = score;
			} else {
				score = 0;
			}
		}
	}
	
	public static void save() {
		Preferences prefs = Gdx.app.getPreferences(NAME);
		prefs.putBoolean(COOKIE_CONSENT, cookieConsent);
		prefs.putBoolean(SOUND_ENABLED, soundEnabled);
		prefs.putBoolean(VIBRATION_ENABLED, vibrationEnabled);
		prefs.putInteger(HIGHSCORE, highScore);
		prefs.putString(CHECKSUM, sha1(secretToken + highScore));
		prefs.flush();
	}
	
	private static String sha1(String text) {
	    String sha1 = "";
	    
	    try {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(text.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest());
	    } catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch(UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    
	    return sha1;
	}

	private static String byteToHex(final byte[] hash) {
	    Formatter formatter = new Formatter();
	    
	    for (byte b : hash) {
	        formatter.format("%02x", b);
	    }
	    
	    String result = formatter.toString();
	    formatter.close();
	    
	    return result;
	}
	
}

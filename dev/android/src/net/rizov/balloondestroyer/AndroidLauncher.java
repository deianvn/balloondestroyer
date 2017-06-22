package net.rizov.balloondestroyer;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import net.rizov.balloondestroyer.config.ConfigManager_Old;
import net.rizov.balloondestroyer.utils.config.ConfigManager;
import net.rizov.balloondestroyer.utils.config.ConfigNameProvider;
import net.rizov.balloondestroyer.utils.helper.Base64;
import net.rizov.balloondestroyer.utils.helper.CryptHelper;
import net.rizov.balloondestroyer.utils.save.BalloonDestroyerGdxPreferencesSaveConnector;
import net.rizov.balloondestroyer.utils.save.BalloonDestroyerSaveManager;
import net.rizov.balloondestroyer.utils.save.SaveConnector;
import net.rizov.balloondestroyer.utils.save.SaveManager;
import net.rizov.balloondestroyer.utils.save.SaveNameProvider;
import net.rizov.gameutils.scene.Factory;
import net.rizov.gameutils.scene.Game;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		final Game game = new Game();
		initialize(new BalloonDestroyer(game), config);

		game.addFactory(Base64.class, new Factory<Base64>() {
			@Override
			public Base64 create() {
				return new Base64() {

					@Override
					public String encode(byte[] data) {
						return android.util.Base64.encodeToString(data, 0);
					}

					@Override
					public byte[] decode(String data) {
						return android.util.Base64.decode(data, 0);
					}
				};
			}
		});

		game.addFactory(SaveConnector.class, new Factory<SaveConnector>() {
			@Override
			public SaveConnector create() {
				return new BalloonDestroyerGdxPreferencesSaveConnector(game);
			}
		});

		game.addFactory(ConfigNameProvider.class, new Factory<ConfigNameProvider>() {
			@Override
			public ConfigNameProvider create() {
				return new ConfigNameProvider() {
					@Override
					public String getName() {
						return "net.rizov.balloondestroyer.config";
					}
				};
			}
		});

		game.addFactory(SaveNameProvider.class, new Factory<SaveNameProvider>() {

			private final SaveNameProvider saveNameProvider = new SaveNameProvider() {
				@Override
				public String getName() {
					return "net.rizov.balloondestroyer.save";
				}
			};

			@Override
			public SaveNameProvider create() {
				return saveNameProvider;
			}
		});

		CryptHelper.injectMembers(game);

		ConfigManager.create(game);
		ConfigManager.load();

		SaveManager.create(game);

		if (!SaveManager.hasSaveData()) {
			//Fallback to old save data manager
			ConfigManager_Old.load();
			SaveManager.load();
			BalloonDestroyerSaveManager.setHighScore(ConfigManager_Old.getHighScore());
			SaveManager.save();
		} else {
			SaveManager.load();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
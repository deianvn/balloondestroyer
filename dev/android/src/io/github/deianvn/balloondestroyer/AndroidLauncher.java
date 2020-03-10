package io.github.deianvn.balloondestroyer;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import io.github.deianvn.balloondestroyer.config.ConfigManager_Old;
import io.github.deianvn.balloondestroyer.utils.config.ConfigManager;
import io.github.deianvn.balloondestroyer.utils.config.ConfigNameProvider;
import io.github.deianvn.balloondestroyer.utils.helper.Base64;
import io.github.deianvn.balloondestroyer.utils.helper.CryptHelper;
import io.github.deianvn.balloondestroyer.utils.save.BalloonDestroyerGdxPreferencesSaveConnector;
import io.github.deianvn.balloondestroyer.utils.save.BalloonDestroyerSaveManager;
import io.github.deianvn.balloondestroyer.utils.save.SaveConnector;
import io.github.deianvn.balloondestroyer.utils.save.SaveManager;
import io.github.deianvn.balloondestroyer.utils.save.SaveNameProvider;
import io.github.deianvn.gameutils.scene.Factory;
import io.github.deianvn.gameutils.scene.Game;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
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
						return "io.github.deianvn.balloondestroyer.config";
					}
				};
			}
		});

		game.addFactory(SaveNameProvider.class, new Factory<SaveNameProvider>() {

			private final SaveNameProvider saveNameProvider = new SaveNameProvider() {
				@Override
				public String getName() {
					return "io.github.deianvn.balloondestroyer.save";
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

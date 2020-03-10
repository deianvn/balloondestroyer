package io.github.deianvn.balloondestroyer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.deianvn.balloondestroyer.BalloonDestroyer;
import io.github.deianvn.balloondestroyer.config.ConfigManager_Old;
import io.github.deianvn.balloondestroyer.room.menu.MenuRoom;
import io.github.deianvn.balloondestroyer.room.play.PlayRoom;
import io.github.deianvn.balloondestroyer.room.play.PlayRoomEvent;
import io.github.deianvn.balloondestroyer.utils.config.ConfigManager;
import io.github.deianvn.balloondestroyer.utils.config.ConfigNameProvider;
import io.github.deianvn.balloondestroyer.utils.helper.Base64;
import io.github.deianvn.balloondestroyer.utils.helper.CryptHelper;
import io.github.deianvn.balloondestroyer.utils.save.BalloonDestroyerGdxPreferencesSaveConnector;
import io.github.deianvn.balloondestroyer.utils.save.BalloonDestroyerSaveManager;
import io.github.deianvn.balloondestroyer.utils.save.SaveConnector;
import io.github.deianvn.balloondestroyer.utils.save.SaveManager;
import io.github.deianvn.balloondestroyer.utils.save.SaveNameProvider;
import io.github.deianvn.gameutils.scene.EventListener;
import io.github.deianvn.gameutils.scene.Factory;
import io.github.deianvn.gameutils.scene.Game;
import io.github.deianvn.gameutils.scene.Room;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 480;
		config.height = 800;

		final Game game = new Game();
		new LwjglApplication(new BalloonDestroyer(game), config);

		game.addFactory(Base64.class, () -> new Base64() {

			@Override
			public String encode(byte[] data) {
				return java.util.Base64.getEncoder().encodeToString(data);
			}

			@Override
			public byte[] decode(String data) {
				return java.util.Base64.getDecoder().decode(data);
			}
		});

		game.addFactory(SaveConnector.class,
				() -> new BalloonDestroyerGdxPreferencesSaveConnector(game));

		game.addFactory(ConfigNameProvider.class, () -> (ConfigNameProvider) () -> "io.github.deianvn.balloondestroyer.config");

		game.addFactory(SaveNameProvider.class, new Factory<SaveNameProvider>() {

			private final SaveNameProvider saveNameProvider = () -> "io.github.deianvn.balloondestroyer.save";

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

		game.addRoomChangeListener(roomChangedEvent -> {
			Room room = roomChangedEvent.getRoom();

			if (room instanceof PlayRoom) {
				PlayRoom playRoom = (PlayRoom) room;

				playRoom.addEventListener(new EventListener<PlayRoomEvent>() {

					private int currentScore = 0;

					@Override
					public void eventHappened(PlayRoomEvent playRoomEvent) {

						boolean clear = false;

						switch (playRoomEvent) {
							case restart:
								clear = true;
							case quit:
							case win:
							case lose:
							case pause:

								int points = playRoomEvent.score - currentScore;

								if (points > 0) {
									currentScore += points;
									//GamePlayServices.addExperience(points);
								}

								if (clear) {
									currentScore = 0;
								}
						}
					}

				});
			} else if (room instanceof MenuRoom) {
				MenuRoom menuRoom = (MenuRoom) room;

				menuRoom.addEventListener(menuRoomEvent -> {

					switch (menuRoomEvent) {
						case showLeaderBoard:

							break;
					}

				});
			}
		});

	}
}

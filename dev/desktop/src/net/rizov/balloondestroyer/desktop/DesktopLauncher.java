package net.rizov.balloondestroyer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.rizov.balloondestroyer.BalloonDestroyer;
import net.rizov.balloondestroyer.config.ConfigManager_Old;
import net.rizov.balloondestroyer.room.menu.MenuRoom;
import net.rizov.balloondestroyer.room.menu.MenuRoomEvent;
import net.rizov.balloondestroyer.room.play.PlayRoom;
import net.rizov.balloondestroyer.room.play.PlayRoomEvent;
import net.rizov.balloondestroyer.utils.config.ConfigManager;
import net.rizov.balloondestroyer.utils.config.ConfigNameProvider;
import net.rizov.balloondestroyer.utils.helper.Base64;
import net.rizov.balloondestroyer.utils.helper.CryptHelper;
import net.rizov.balloondestroyer.utils.save.*;
import net.rizov.gameutils.scene.*;

import javax.xml.bind.DatatypeConverter;

public class DesktopLauncher {
    public static void main(String[] arg) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 480;
        config.height = 800;

        final Game game = new Game();
        new LwjglApplication(new BalloonDestroyer(game), config);

        game.addFactory(Base64.class, new Factory<Base64>() {
            @Override
            public Base64 create() {
                return new Base64() {

                    @Override
                    public String encode(byte[] data) {
                        return DatatypeConverter.printBase64Binary(data);
                    }

                    @Override
                    public byte[] decode(String data) {
                        return DatatypeConverter.parseBase64Binary(data);
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

        game.addRoomChangeListener(new RoomChangeListener() {

            @Override
            public void roomChanged(RoomChangedEvent roomChangedEvent) {
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

                    menuRoom.addEventListener(new EventListener<MenuRoomEvent>() {
                        @Override
                        public void eventHappened(MenuRoomEvent menuRoomEvent) {

                            switch (menuRoomEvent) {
                                case showLeaderBoard:

                                    break;
                            }

                        }
                    });
                }
            }
        });

    }
}

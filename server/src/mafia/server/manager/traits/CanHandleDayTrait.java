package mafia.server.manager.traits;

import mafia.server.ChatSystem.ChatServer;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.File;
import java.util.concurrent.TimeUnit;

public interface CanHandleDayTrait {
    /**
     * Handle day.
     *
     * @param database the database
     */
    default void handleDay(File database) {
        ChatServer chatServer = new ChatServer(database);

        for (PlayerWorker alivePlayer : GameState.getSingletonInstance().getAlivePlayers()) {
            if (alivePlayer.getGameRoll().canSpeak()) {
                chatServer.addUser(alivePlayer);
            }
        }

        chatServer.start(5, TimeUnit.MINUTES);
    }
}

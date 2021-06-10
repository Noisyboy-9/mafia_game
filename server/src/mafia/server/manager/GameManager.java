package mafia.server.manager;

import mafia.server.ChatSystem.ChatServer;
import mafia.server.manager.traits.CanHandleIntroductionNightTrait;
import mafia.server.manager.traits.CanHandleNightTrait;
import mafia.server.manager.traits.CanHandlePollTrait;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * The type Game manager.
 */
public class GameManager implements CanHandleNightTrait,
        CanHandleIntroductionNightTrait,
        CanHandlePollTrait {
    /**
     * Handle day.
     *
     * @param database the database
     */
    public void handleDay(File database) {
        ChatServer chatServer = new ChatServer(database);

        for (PlayerWorker alivePlayer : GameState.getSingletonInstance().getAlivePlayers()) {
            if (alivePlayer.getGameRoll().canSpeak()) {
                chatServer.addUser(alivePlayer);
            }
        }

        chatServer.start(5, TimeUnit.MINUTES);
    }
}
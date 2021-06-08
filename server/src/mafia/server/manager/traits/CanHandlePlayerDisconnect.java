package mafia.server.manager.traits;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * The interface Can handle player disconnect.
 */
public interface CanHandlePlayerDisconnect {
    /**
     * Handle player disconnect.
     *
     * @param playerWorker the player worker
     */
    default void handlePlayerDisconnect(PlayerWorker playerWorker) {
        this.broadcastMessageToAll(playerWorker.getUsername());
        try {
            GameState.getSingletonInstance().killPlayer(playerWorker);
        } catch (PlayerIsAlreadyDeadException ignored) {
        }
    }

    /**
     * Broadcast message to all.
     *
     * @param message the message
     */
    default void broadcastMessageToAll(String message) {
        ArrayList<PlayerWorker> playerWorkers = GameState.getSingletonInstance().getAllGamePlayers();
        for (PlayerWorker playerWorker : playerWorkers) {
            ObjectOutputStream response = playerWorker.getResponse();

            try {
                response.writeObject(new ShowMessageCommand(message).toString());
            } catch (IOException ignored) {
            }
        }
    }
}

package mafia.server.player.traits;

import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * The interface Can select player trait.
 */
public interface CanSelectPlayerTrait {
    /**
     * Gets player username.
     *
     * @param playerWorker the player worker
     * @return the player username
     */
    default String getPlayerUsername(PlayerWorker playerWorker) {
        String votedForUsername = null;
        ObjectInputStream request = playerWorker.getRequest();
        try {
            votedForUsername = (String) request.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        return votedForUsername;
    }
}

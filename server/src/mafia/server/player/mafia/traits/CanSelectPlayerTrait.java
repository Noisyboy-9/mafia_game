package mafia.server.player.mafia.traits;

import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;

public interface CanSelectPlayerTrait {
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

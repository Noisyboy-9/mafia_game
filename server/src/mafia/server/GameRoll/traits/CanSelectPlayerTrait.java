package mafia.server.GameRoll.traits;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
    default PlayerWorker getSelectedPlayer(PlayerWorker playerWorker) {
        String votedForUsername = null;
        ObjectInputStream request = playerWorker.getRequest();
        try {
            votedForUsername = (String) request.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        while (!GameState.playerWithUsernameExist(votedForUsername)) {
            ObjectOutputStream response = playerWorker.getResponse();
            try {
                response.writeObject(new ShowMessageCommand("User not found").toString());
                votedForUsername = (String) request.readObject();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        }

        return GameState.getPlayerWorkerByUsername(votedForUsername);
    }
}

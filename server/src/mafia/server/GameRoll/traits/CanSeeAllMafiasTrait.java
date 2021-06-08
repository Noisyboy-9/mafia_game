package mafia.server.GameRoll.traits;

import mafia.server.commands.GetInputCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The interface Can see all mafias trait.
 */
public interface CanSeeAllMafiasTrait {
    /**
     * Show all mafias to client.
     *
     * @param playerWorker the player worker
     */
    default void showAllMafiasToClient(PlayerWorker playerWorker) {
        try {
            ObjectOutputStream response = playerWorker.getResponse();
            response.writeObject(new ShowMessageCommand("all available players").toString());
            response.writeObject(new ShowMessageCommand(GameState.getSingletonInstance().aliveMafiasToString()).toString());
            response.writeObject(new GetInputCommand("choose one to kill").toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

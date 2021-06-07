package mafia.server.GameRoll.traits;

import mafia.server.commands.GetInputCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The interface Can see all players trait.
 */
public interface CanSeeAllPlayersTrait {
    /**
     * Show all players to client.
     *
     * @param playerWorker the player worker
     */
    default void showAllPlayersToClient(PlayerWorker playerWorker) {
        ObjectOutputStream response = playerWorker.getResponse();

        try {
            response.writeObject(new ShowMessageCommand("all available players").toString());
            response.writeObject(new ShowMessageCommand(GameState.alivePlayersToString()).toString());
            response.writeObject(new GetInputCommand("choose one to kill").toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

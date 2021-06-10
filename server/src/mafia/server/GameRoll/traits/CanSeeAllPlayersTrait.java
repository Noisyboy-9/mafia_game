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
//            show all alive mafias
            response.writeObject(new ShowMessageCommand("All alive mafias:").toString());
            response.writeObject(new ShowMessageCommand(GameState.getSingletonInstance().aliveMafiasToString()).toString());
            response.writeObject(new ShowMessageCommand("-----------------------------------").toString());

//            show all alive citizens
            response.writeObject(new ShowMessageCommand("all available citizens:").toString());
            response.writeObject(new ShowMessageCommand(GameState.getSingletonInstance().aliveCitizensToString()).toString());
            response.writeObject(new GetInputCommand("Please enter selected player username").toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

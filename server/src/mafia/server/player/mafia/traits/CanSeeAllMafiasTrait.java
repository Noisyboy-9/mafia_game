package mafia.server.player.mafia.traits;

import mafia.server.commands.GetInputCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface CanSeeAllMafiasTrait {
    default void showAllMafiasToClient(PlayerWorker playerWorker) {
        ObjectOutputStream response = playerWorker.getResponse();

        try {
            response.writeObject(new ShowMessageCommand("all available players"));
            response.writeObject(new ShowMessageCommand(GameState.aliveMafiasToString()).toString());
            response.writeObject(new GetInputCommand("choose one to kill"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

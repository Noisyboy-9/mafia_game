package mafia.server.GameRoll.traits;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The interface Can vote for kill target trait.
 */
public interface CanVoteForKillTargetTrait extends CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    /**
     * Vote for citizen to kill player worker.
     *
     * @param playerWorker the player worker
     * @return the player worker
     */
    default PlayerWorker voteForCitizenToKill(PlayerWorker playerWorker) {
        this.showAllPlayersToClient(playerWorker);
        PlayerWorker killTarget = this.getSelectedPlayer(playerWorker);

        while (!GameState.usernameExist(killTarget.getUsername())) {
            ObjectOutputStream response = playerWorker.getResponse();

            try {
                response.writeObject(new ShowMessageCommand("Player does not exist").toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            this.showAllPlayersToClient(playerWorker);
            killTarget = this.getSelectedPlayer(playerWorker);
        }

        return killTarget;
    }
}

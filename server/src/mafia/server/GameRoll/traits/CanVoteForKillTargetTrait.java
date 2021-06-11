package mafia.server.GameRoll.traits;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The interface Can vote for kill target trait.
 */
public interface CanVoteForKillTargetTrait extends CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    /**
     * Vote for citizen to kill player worker.
     *
     * @param mafiaWorker the player worker
     * @return the player worker
     */
    default PlayerWorker voteForCitizenToKill(PlayerWorker mafiaWorker) {
        this.showAllPlayersToClient(mafiaWorker);
        PlayerWorker killTarget = this.getSelectedPlayer(mafiaWorker);
        ObjectOutputStream response = mafiaWorker.getResponse();
        ObjectInputStream request = mafiaWorker.getRequest();

        while (mafiaWorker.equals(killTarget)) {
            try {
                response.writeObject(new ShowMessageCommand("Mafia can't kill mafia.").toString());
                response.writeObject(new ShowMessageCommand("Please input again.").toString());
                killTarget = this.getSelectedPlayer(mafiaWorker);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        while (!GameState.getSingletonInstance().playerWithUsernameExist(killTarget.getUsername())) {
            try {
                response.writeObject(new ShowMessageCommand("Player does not exist").toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            this.showAllPlayersToClient(mafiaWorker);
            killTarget = this.getSelectedPlayer(mafiaWorker);
        }

        return killTarget;
    }
}

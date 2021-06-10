package mafia.server.GameRoll.citizen;

import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.GameRoll.traits.CanSeeAllPlayersTrait;
import mafia.server.GameRoll.traits.CanSelectPlayerTrait;
import mafia.server.commands.GetInputCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The type Sniper.
 */
public class Sniper extends Citizen implements CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    /**
     * Want to act boolean.
     *
     * @param sniper the sniper
     * @return the boolean
     */
    public boolean wantToAct(PlayerWorker sniper) {
        ObjectOutputStream response = sniper.getResponse();

        try {
            response.writeObject(new GetInputCommand("want to take shot?(Y for yes, N for no)").toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        ObjectInputStream request = sniper.getRequest();
        try {
            String mayorDecision = (String) request.readObject();
            return mayorDecision.equalsIgnoreCase("y");
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        this.sendVoteReceivedNotification(sniper);
        return false;
    }

    /**
     * Shoot player.
     *
     * @param sniper the sniper
     * @return the player worker
     */
    public PlayerWorker shootPlayer(PlayerWorker sniper) {
        this.showAllPlayersToClient(sniper);
        PlayerWorker shootTarget = this.getSelectedPlayer(sniper);

        if (shootTarget.getGameRoll().isMafia()) {
            shootTarget.getGameRoll().kill();
            return shootTarget;
        }

//            sniper has tried to shoot a citizen so sniper itself has to be killed.
        sniper.getGameRoll().kill();
        ObjectOutputStream response = sniper.getResponse();
        try {
            response.writeObject(new ShowMessageCommand("Wrong shot! you are killed!").toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return sniper;
    }
}

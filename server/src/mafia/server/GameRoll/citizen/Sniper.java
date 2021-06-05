package mafia.server.GameRoll.citizen;

import mafia.server.commands.GetInputCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.GameRoll.GameRoll;
import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.GameRoll.traits.CanSeeAllPlayersTrait;
import mafia.server.GameRoll.traits.CanSelectPlayerTrait;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Sniper extends Citizen implements CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    public boolean wantToAct(PlayerWorker sniper) {
        ObjectOutputStream response = sniper.getResponse();

        try {
            response.writeObject(new GetInputCommand("want to take shot?(Y for yes, N for no)"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        ObjectInputStream request = mayor.getRequest();
        try {
            String mayorDecision = (String) request.readObject();
            return mayorDecision.equalsIgnoreCase("y");
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }

    public void shootPlayer(PlayerWorker sniper) {
        this.showAllPlayersToClient(sniper);
        String shootTargetUsername = this.getPlayerUsername(sniper);
        GameRoll shootTarget = GameState.getPlayerWorkerByUsername(shootTargetUsername).getPlayer();

        if (shootTarget.isMafia()) {
            shootTarget.kill();
            return;
        }

//            sniper has tried to shoot a citizen so sniper itself has to be killed.
        sniper.getPlayer().kill();
        ObjectOutputStream response = sniper.getResponse();
        try {
            response.writeObject(new ShowMessageCommand("Wrong shot! you are killed!").toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

package mafia.server.player.citizen;

import mafia.server.player.Player;
import mafia.server.player.citizen.abstracts.Citizen;
import mafia.server.player.traits.CanSeeAllPlayersTrait;
import mafia.server.player.traits.CanSelectPlayerTrait;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

public class CityDoctor extends Citizen implements CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    private boolean hasCuredHimself = false;

    public void selectPlayerToCure(PlayerWorker cityDoctor) {
        this.showAllPlayersToClient(cityDoctor);
        String cureTargetUsername = this.getPlayerUsername(cityDoctor);
        Player player = GameState.getPlayerWorkerByUsername(cureTargetUsername).getPlayer();

        while (cityDoctor.getUsername().equals(cureTargetUsername) && this.hasCuredHimself) {
//            doctor lector has cured himself once in the past can not cure himself again.
            this.showAllMafiasToClient(doctorLector);
            cureTargetUsername = this.getPlayerUsername(doctorLector);
            cureTarget = GameState.getPlayerWorkerByUsername(cureTargetUsername).getPlayer();
        }

        if (cityDoctor.getUsername().equals(cureTargetUsername)) {
            this.hasCuredHimself = true;
        } else {
            player.revive();
        }
    }
}

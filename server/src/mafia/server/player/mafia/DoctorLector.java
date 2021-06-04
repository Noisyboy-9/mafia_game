package mafia.server.player.mafia;

import mafia.server.player.Player;
import mafia.server.player.mafia.abstacts.Mafia;
import mafia.server.player.mafia.traits.CanSeeAllMafiasTrait;
import mafia.server.player.mafia.traits.CanSelectPlayerTrait;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

public class DoctorLector extends Mafia implements CanSeeAllMafiasTrait, CanSelectPlayerTrait {
    private boolean hasCuredHimself = false;

    public void selectMafiaToCure(PlayerWorker doctorLector) {
        this.showAllMafiasToClient(doctorLector);
        String cureTargetUsername = this.getPlayerUsername(doctorLector);

        Player cureTarget = GameState.getPlayerByUsername(cureTargetUsername).getPlayer();

        while (!cureTarget.isMafia()) {
            this.showAllMafiasToClient(doctorLector);
            cureTargetUsername = this.getPlayerUsername(doctorLector);
            cureTarget = GameState.getPlayerByUsername(cureTargetUsername).getPlayer();
        }

        cureTarget.revive();
    }
}

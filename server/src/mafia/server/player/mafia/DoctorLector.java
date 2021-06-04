package mafia.server.player.mafia;

import mafia.server.player.Player;
import mafia.server.player.mafia.abstacts.Mafia;
import mafia.server.player.traits.CanSeeAllMafiasTrait;
import mafia.server.player.traits.CanSelectPlayerTrait;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

/**
 * The role DoctorLector in mafia game.
 */
public class DoctorLector extends Mafia implements CanSeeAllMafiasTrait, CanSelectPlayerTrait {
    private boolean hasCuredHimself = false;

    /**
     * Select mafia to cure.
     *
     * @param doctorLector the doctor lector
     */
    public void selectMafiaToCure(PlayerWorker doctorLector) {
        this.showAllMafiasToClient(doctorLector);
        String cureTargetUsername = this.getPlayerUsername(doctorLector);
        Player cureTarget = GameState.getPlayerByUsername(cureTargetUsername).getPlayer();

        while (!cureTarget.isMafia()) {
            this.showAllMafiasToClient(doctorLector);
            cureTargetUsername = this.getPlayerUsername(doctorLector);
            cureTarget = GameState.getPlayerByUsername(cureTargetUsername).getPlayer();
        }

        while (doctorLector.getUsername().equals(cureTargetUsername) && this.hasCuredHimself) {
//            doctor lector has cured himself once in the past can not cure himself again.
            this.showAllMafiasToClient(doctorLector);
            cureTargetUsername = this.getPlayerUsername(doctorLector);
            cureTarget = GameState.getPlayerByUsername(cureTargetUsername).getPlayer();
        }


        if (doctorLector.getUsername().equals(cureTargetUsername)) {
            this.hasCuredHimself = true;
        } else {
            cureTarget.revive();
        }
    }
}

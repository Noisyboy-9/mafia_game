package mafia.server.GameRoll.mafia;

import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.GameRoll.traits.CanSeeAllMafiasTrait;
import mafia.server.GameRoll.traits.CanSelectMafiaTrait;
import mafia.server.workers.PlayerWorker;

/**
 * The role DoctorLector in mafia game.
 */
public class DoctorLector extends Mafia implements CanSeeAllMafiasTrait, CanSelectMafiaTrait {
    /**
     * Select mafia to cure.
     *
     * @param doctorLector the doctor lector
     */
    public PlayerWorker selectMafiaToCure(PlayerWorker doctorLector) {
        this.showAllMafiasToClient(doctorLector);
        PlayerWorker cureTarget = this.getSelectedMafia(doctorLector);

        while (doctorLector.getUsername().equals(cureTarget.getUsername())) {
//            doctor lector has cured himself once in the past can not cure himself again.
            this.showAllMafiasToClient(doctorLector);
            cureTarget = this.getSelectedPlayer(doctorLector);
        }

        return cureTarget;
    }
}

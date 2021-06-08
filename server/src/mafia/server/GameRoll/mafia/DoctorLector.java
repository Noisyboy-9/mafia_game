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
        return this.getSelectedMafia(doctorLector);
    }
}

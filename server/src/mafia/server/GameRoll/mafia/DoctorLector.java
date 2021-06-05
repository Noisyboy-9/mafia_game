package mafia.server.GameRoll.mafia;

import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.GameRoll.traits.CanSeeAllMafiasTrait;
import mafia.server.GameRoll.traits.CanSelectPlayerTrait;
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
        PlayerWorker cureTarget = this.getSelectedPlayer(doctorLector);

        while (!cureTarget.getGameRoll().isMafia()) {
            this.showAllMafiasToClient(doctorLector);
            cureTarget = this.getSelectedPlayer(doctorLector);
        }

        while (doctorLector.getUsername().equals(cureTarget.getUsername()) && this.hasCuredHimself) {
//            doctor lector has cured himself once in the past can not cure himself again.
            this.showAllMafiasToClient(doctorLector);
            cureTarget = this.getSelectedPlayer(doctorLector);
        }


        if (doctorLector.getUsername().equals(cureTarget.getUsername())) {
            this.hasCuredHimself = true;
        } else {
            cureTarget.getGameRoll().revive();
        }
    }
}

package mafia.server.GameRoll.citizen;

import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.GameRoll.traits.CanSeeAllPlayersTrait;
import mafia.server.GameRoll.traits.CanSelectPlayerTrait;
import mafia.server.workers.PlayerWorker;

/**
 * The type City doctor.
 */
public class CityDoctor extends Citizen implements CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    private boolean hasCuredHimself = false;

    /**
     * Select player to cure.
     *
     * @param cityDoctor the city doctor
     */
    public void selectPlayerToCure(PlayerWorker cityDoctor) {
        this.showAllPlayersToClient(cityDoctor);
        PlayerWorker cureTarget = this.getSelectedPlayer(cityDoctor);

        while (cityDoctor.getUsername().equals(cureTarget.getUsername()) && this.hasCuredHimself) {
//            doctor lector has cured himself once in the past can not cure himself again.
            this.showAllPlayersToClient(cityDoctor);
            cureTarget = this.getSelectedPlayer(cityDoctor);
        }

        if (cityDoctor.getUsername().equals(cureTarget.getUsername())) {
            this.hasCuredHimself = true;
        } else {
            cureTarget.getGameRoll().revive();
        }
    }
}

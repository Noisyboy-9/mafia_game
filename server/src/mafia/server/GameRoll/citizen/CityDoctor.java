package mafia.server.GameRoll.citizen;

import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.GameRoll.traits.CanSeeAllPlayersTrait;
import mafia.server.GameRoll.traits.CanSelectPlayerTrait;
import mafia.server.workers.PlayerWorker;

/**
 * The type City doctor.
 */
public class CityDoctor extends Citizen implements CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    /**
     * Select player to cure.
     *
     * @param cityDoctor the city doctor
     */
    public PlayerWorker selectPlayerToCure(PlayerWorker cityDoctor) {
        this.showAllPlayersToClient(cityDoctor);
        PlayerWorker cureTarget = this.getSelectedPlayer(cityDoctor);

        while (cityDoctor.getUsername().equals(cureTarget.getUsername())) {
//            doctor lector has cured himself once in the past can not cure himself again.
            this.showAllPlayersToClient(cityDoctor);
            cureTarget = this.getSelectedPlayer(cityDoctor);
        }

        return cureTarget;
    }
}

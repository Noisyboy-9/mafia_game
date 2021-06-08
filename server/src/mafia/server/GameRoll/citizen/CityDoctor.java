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
        return this.getSelectedPlayer(cityDoctor);
    }
}

package mafia.server.GameRoll.citizen;

import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.GameRoll.traits.CanSeeAllPlayersTrait;
import mafia.server.GameRoll.traits.CanSelectPlayerTrait;
import mafia.server.workers.PlayerWorker;

/**
 * The type Psychiatrist.
 */
public class Psychiatrist extends Citizen implements CanSelectPlayerTrait, CanSeeAllPlayersTrait {
    /**
     * Select player to mute.
     *
     * @param psychiatrist the psychiatrist
     */
    public void selectPlayerToMute(PlayerWorker psychiatrist) {
        this.showAllPlayersToClient(psychiatrist);
        PlayerWorker muteTarget = this.getSelectedPlayer(psychiatrist);
        muteTarget.getGameRoll().mute();
    }

}

package mafia.server.player.citizen;

import mafia.server.player.Player;
import mafia.server.player.citizen.abstracts.Citizen;
import mafia.server.player.traits.CanSeeAllPlayersTrait;
import mafia.server.player.traits.CanSelectPlayerTrait;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

public class Psychiatrist extends Citizen implements CanSelectPlayerTrait, CanSeeAllPlayersTrait {
    public void selectPlayerToMute(PlayerWorker psychiatrist) {
        this.showAllPlayersToClient(psychiatrist);
        String muteTargetUsername = this.getPlayerUsername(psychiatrist);
        Player muteTarget = GameState.getPlayerWorkerByUsername(muteTargetUsername).getPlayer();
        muteTarget.mute();
    }

}

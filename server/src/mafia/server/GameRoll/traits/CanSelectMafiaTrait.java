package mafia.server.GameRoll.traits;

import mafia.server.workers.PlayerWorker;

public interface CanSelectMafiaTrait extends CanSelectPlayerTrait, CanSeeAllPlayersTrait {

    default PlayerWorker getSelectedMafia(PlayerWorker mafia) {
        PlayerWorker otherMafia = this.getSelectedPlayer(mafia);

        while (!otherMafia.getGameRoll().isMafia()) {
            this.showAllPlayersToClient(mafia);
            otherMafia = this.getSelectedPlayer(mafia);
        }

        return otherMafia;
    }
}

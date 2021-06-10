package mafia.server.GameRoll.traits;

import mafia.server.workers.PlayerWorker;

/**
 * The interface Can select mafia trait.
 */
public interface CanSelectMafiaTrait extends CanSelectPlayerTrait, CanSeeAllPlayersTrait {

    /**
     * Gets selected mafia.
     *
     * @param mafia the mafia
     * @return the selected mafia
     */
    default PlayerWorker getSelectedMafia(PlayerWorker mafia) {
        PlayerWorker otherMafia = this.getSelectedPlayer(mafia);

        while (!otherMafia.getGameRoll().isMafia()) {
            this.showAllPlayersToClient(mafia);
            otherMafia = this.getSelectedPlayer(mafia);
        }

        this.sendVoteReceivedNotification(mafia);
        return otherMafia;
    }
}

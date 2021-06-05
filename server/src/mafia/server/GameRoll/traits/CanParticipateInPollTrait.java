package mafia.server.GameRoll.traits;

import mafia.server.workers.PlayerWorker;

/**
 * The interface Can participate in poll trait.
 */
public interface CanParticipateInPollTrait extends CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    /**
     * Vote in poll player worker.
     *
     * @param voter the voter
     * @return the player worker
     */
    default PlayerWorker voteInPoll(PlayerWorker voter) {
        this.showAllPlayersToClient(voter);
        return this.getSelectedPlayer(voter);
    }
}

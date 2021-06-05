package mafia.server.GameRoll.traits;

import mafia.server.workers.PlayerWorker;

public interface CanParticipateInPollTrait extends CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    default PlayerWorker voteInPoll(PlayerWorker voter) {
        this.showAllPlayersToClient(voter);
        return this.getSelectedPlayer(voter);
    }
}

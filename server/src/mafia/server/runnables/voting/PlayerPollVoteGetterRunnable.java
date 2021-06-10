package mafia.server.runnables.voting;

import mafia.server.GameRoll.GameRoll;
import mafia.server.manager.traits.CanHandlePlayerDisconnect;
import mafia.server.workers.PlayerWorker;

import java.util.HashMap;

/**
 * The type Player poll vote getter runnable.
 */
public class PlayerPollVoteGetterRunnable implements Runnable, CanHandlePlayerDisconnect {
    private final PlayerWorker voterWorker;
    private final HashMap<PlayerWorker, Integer> votes;

    /**
     * Instantiates a new Player poll vote getter runnable.
     *
     * @param voterWorker the voter worker
     * @param votes       the votes
     */
    public PlayerPollVoteGetterRunnable(PlayerWorker voterWorker, HashMap<PlayerWorker, Integer> votes) {
        this.voterWorker = voterWorker;
        this.votes = votes;
    }

    @Override
    public void run() {
        GameRoll gameRoll = voterWorker.getGameRoll();
        PlayerWorker voteTarget = gameRoll.voteInPoll(this.voterWorker);

        if (votes.containsKey(voteTarget)) {
            int votesCount = votes.get(voteTarget);
            votesCount++;
            this.votes.put(voteTarget, votesCount);
        } else {
            votes.put(voteTarget, 1);
        }

        this.broadcastMessageToAll(voterWorker.getUsername() + " has voted for: " + voteTarget.getUsername());
    }
}

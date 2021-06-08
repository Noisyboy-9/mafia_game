package mafia.server.runnables;

import mafia.server.GameRoll.GameRoll;
import mafia.server.workers.PlayerWorker;

import java.util.HashMap;

public class PlayerPollVoteGetterRunnable implements Runnable {
    private final PlayerWorker voterWorker;
    private final HashMap<PlayerWorker, Integer> votes;

    public PlayerPollVoteGetterRunnable(PlayerWorker voterWorker, HashMap<PlayerWorker, Integer> votes) {
        this.voterWorker = voterWorker;
        this.votes = votes;
    }

    @Override
    public void run() {
        GameRoll gameRoll = voterWorker.getGameRoll();
        PlayerWorker voteTarget = gameRoll.voteInPoll(this.voterWorker);

        synchronized (votes) {
            if (votes.containsKey(voteTarget)) {
                int votesCount = votes.get(voteTarget);
                votesCount++;
                this.votes.put(voteTarget, votesCount);
            } else {
                votes.put(voteTarget, 1);
            }
        }
    }
}

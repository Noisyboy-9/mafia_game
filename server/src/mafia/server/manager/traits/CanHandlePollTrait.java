package mafia.server.manager.traits;

import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.runnables.voting.PlayerPollVoteGetterRunnable;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public interface CanHandlePollTrait extends CanHandlePlayerDisconnect {
    default void handlePoll() {
        ArrayList<PlayerWorker> players = GameState.getSingletonInstance().getAlivePlayers();
        HashMap<PlayerWorker, Integer> votes = this.getPlayersVotes(players);

        Map.Entry<PlayerWorker, Integer> highestVote = null;

        for (Map.Entry<PlayerWorker, Integer> vote : votes.entrySet()) {
            if (highestVote == null || vote.getValue().compareTo(highestVote.getValue()) > 0) {
                highestVote = vote;
            }
        }

        PlayerWorker votesKillTarget = Objects.requireNonNull(highestVote).getKey();

        try {
            GameState.getSingletonInstance().killPlayer(votesKillTarget);
        } catch (PlayerIsAlreadyDeadException e) {
            e.printStackTrace();
        }

        this.broadcastMessageToAll("In the votes " + votesKillTarget.getUsername() + " was killed!");
    }

    private HashMap<PlayerWorker, Integer> getPlayersVotes(ArrayList<PlayerWorker> players) {
        ExecutorService executor = Executors.newCachedThreadPool();
        HashMap<PlayerWorker, Integer> votes = new HashMap<>();

        for (PlayerWorker playerWorker : players) {
            executor.execute(new PlayerPollVoteGetterRunnable(playerWorker, votes));
        }

        try {
            executor.shutdown();
            executor.awaitTermination(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return votes;
    }
}

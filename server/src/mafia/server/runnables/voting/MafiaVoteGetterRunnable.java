package mafia.server.runnables.voting;

import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.workers.PlayerWorker;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Mafia vote getter runnable.
 */
public class MafiaVoteGetterRunnable implements Runnable {
    private final PlayerWorker mafiaWorker;
    private final ConcurrentHashMap<PlayerWorker, PlayerWorker> votes;

    /**
     * Instantiates a new Mafia vote getter runnable.
     *
     * @param mafiaWorker the mafia worker
     * @param votes       the votes
     */
    public MafiaVoteGetterRunnable(PlayerWorker mafiaWorker, ConcurrentHashMap<PlayerWorker, PlayerWorker> votes) {
        this.mafiaWorker = mafiaWorker;
        this.votes = votes;
    }

    @Override
    public void run() {
        Mafia mafia = (Mafia) this.mafiaWorker.getGameRoll();
        PlayerWorker voteTarget = mafia.voteForCitizenToKill(this.mafiaWorker);

        synchronized (votes) {
            this.votes.put(this.mafiaWorker, voteTarget);
        }
    }
}

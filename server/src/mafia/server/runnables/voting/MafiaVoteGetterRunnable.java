package mafia.server.runnables.voting;

import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.workers.PlayerWorker;

import java.util.HashMap;

public class MafiaVoteGetterRunnable implements Runnable {
    private final PlayerWorker mafiaWorker;
    private final HashMap<PlayerWorker, PlayerWorker> votes;

    public MafiaVoteGetterRunnable(PlayerWorker mafiaWorker, HashMap<PlayerWorker, PlayerWorker> votes) {
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

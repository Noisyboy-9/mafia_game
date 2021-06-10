package mafia.server.GameRoll.mafia.abstacts;

import mafia.server.GameRoll.GameRoll;
import mafia.server.GameRoll.traits.CanVoteForKillTargetTrait;
import mafia.server.commands.GetInputCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.exceptions.BottomMafiaCanNotKillCitizenException;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Mafia role of the game.
 */
public abstract class Mafia extends GameRoll implements CanVoteForKillTargetTrait {
    /**
     * The Is leader.
     */
    protected boolean isLeader = false;

    /**
     * Is leader boolean.
     *
     * @return the boolean
     */
    public boolean isLeader() {
        return isLeader;
    }

    @Override
    public void kill() {
        if (this.isLeader) {
            this.isLeader = false;
        }
        GameState.getSingletonInstance().setNewMafiaLeader();
        super.kill();
    }

    /**
     * Select citizen to kill.
     *
     * @param leaderWorker the leader worker
     * @return the player worker
     * @throws IOException the io exception
     */
    public PlayerWorker selectCitizenToKill(PlayerWorker leaderWorker) throws IOException, BottomMafiaCanNotKillCitizenException {
        Mafia leader = (Mafia) leaderWorker.getGameRoll();
        if (!leader.isLeader) {
            throw new BottomMafiaCanNotKillCitizenException(leaderWorker.getUsername() + " is not leader.It can not kill a citizen!");
        }

        ObjectOutputStream response = leaderWorker.getResponse();
        ObjectInputStream request = leaderWorker.getRequest();

        response.writeObject(new ShowMessageCommand(GameState.getSingletonInstance().aliveCitizensToString()).toString());
        response.writeObject(new GetInputCommand("Who do you want to kill?(please enter username) ").toString());

        String username = null;
        try {
            username = (String) request.readObject();
            while (!GameState.getSingletonInstance().playerWithUsernameExist(username)) {
                response.writeObject(new ShowMessageCommand("invalid username!").toString());
                response.writeObject(new ShowMessageCommand(GameState.getSingletonInstance().aliveCitizensToString()).toString());
                response.writeObject(new GetInputCommand("Who do you want to kill?(please enter username) ").toString());

                username = (String) request.readObject();
            }
        } catch (ClassNotFoundException ignored) {
        }

        PlayerWorker killTarget = GameState.getSingletonInstance().getPlayerWorkerByUsername(username);
        while (killTarget.getGameRoll().isMafia()) {
            response.writeObject(new ShowMessageCommand("Mafia can't kill another mafia").toString());
            response.writeObject(new ShowMessageCommand(GameState.getSingletonInstance().aliveCitizensToString()).toString());
            response.writeObject(new GetInputCommand("Who do you want to kill?(please enter username) ").toString());
            try {
                username = (String) request.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        this.sendVoteReceivedNotification(leaderWorker);
        return GameState.getSingletonInstance().getPlayerWorkerByUsername(username);
    }

    /**
     * Make this the leader of mafias.
     */
    public void promoteToMafiaLeader() {
        this.isLeader = true;
    }

    /**
     * Show other mafias vote.
     *
     * @param votes           the votes
     * @param godFatherWorker the god father worker
     * @throws IOException the io exception
     */
    public void showOtherMafiasVote(Map<PlayerWorker, PlayerWorker> votes, PlayerWorker godFatherWorker) throws IOException {
        ObjectOutputStream response = godFatherWorker.getResponse();
        for (Map.Entry<PlayerWorker, PlayerWorker> vote : votes.entrySet()) {
            PlayerWorker voter = vote.getKey();
            PlayerWorker voteTarget = vote.getValue();

            response.writeObject(new ShowMessageCommand(voter.getUsername() + " has voted for " + voteTarget.getUsername()).toString());
        }
    }
}

package mafia.server.state.managers;

import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.exceptions.PlayerAlreadyExistException;
import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.workers.PlayerWorker;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerWorkerManager {
    private final ArrayList<PlayerWorker> alivePlayers;
    private final ArrayList<PlayerWorker> deadPlayers;


    public PlayerWorkerManager() {
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
    }

    public ArrayList<PlayerWorker> getAlivePlayers() {
        return alivePlayers;
    }

    public ArrayList<PlayerWorker> getDeadPlayers() {
        return deadPlayers;
    }

    /**
     * get all players in the game.
     *
     * @return the array list
     */
    public ArrayList<PlayerWorker> getAllGamePlayers() {
        alivePlayers.addAll(deadPlayers);
        return alivePlayers;
    }

    /**
     * Add player.
     *
     * @param playerWorker the player worker
     * @throws PlayerAlreadyExistException the player already exist exception
     */
    public void addPlayer(PlayerWorker playerWorker) throws PlayerAlreadyExistException {
        if (this.alivePlayers.contains(playerWorker)) {
            throw new PlayerAlreadyExistException("Can not add already added player to game twice");
        }

        this.alivePlayers.add(playerWorker);
    }

    /**
     * Kill player.
     *
     * @param killTarget the kill target
     * @throws PlayerIsAlreadyDeadException the player is already dead exception
     */
    public void killPlayer(PlayerWorker killTarget) throws PlayerIsAlreadyDeadException {
        if (this.deadPlayers.contains(killTarget)) {
            throw new PlayerIsAlreadyDeadException("Player has been already killed can't kill him twice!");
        }

        killTarget.getGameRoll().kill();
        this.alivePlayers.remove(killTarget);
        this.deadPlayers.add(killTarget);
    }

    /**
     * Gets alive mafias.
     *
     * @return the alive mafias
     */
    public ArrayList<PlayerWorker> getAliveMafias() {
        ArrayList<PlayerWorker> mafias = new ArrayList<>();

        for (PlayerWorker playerWorker : this.alivePlayers) {
            if (playerWorker.getGameRoll().isMafia()) mafias.add(playerWorker);
        }

        return mafias;
    }

    /**
     * Gets alive citizens.
     *
     * @return the alive citizens
     */
    public ArrayList<PlayerWorker> getAliveCitizens() {
        ArrayList<PlayerWorker> citizens = new ArrayList<>();

        for (PlayerWorker playerWorker : this.alivePlayers) {
            if (playerWorker.getGameRoll().isCitizen()) citizens.add(playerWorker);
        }

        return citizens;
    }

    /**
     * Alive players to string string.
     *
     * @return the string
     */
    public String alivePlayersToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("All Alive Players: \n");

        for (PlayerWorker playerWorker : this.alivePlayers) {
            builder.append(playerWorker.toString());
            builder.append("\n");
        }

//        removing last unnecessary \n character
        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    /**
     * Alive mafias to string string.
     *
     * @return the string
     */
    public String aliveMafiasToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("All Alive Mafias: \n");

        for (PlayerWorker playerWorker : getAliveMafias()) {
            builder.append(playerWorker);
            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * Mafia count int.
     *
     * @return the int
     */
    public int aliveMafiaCount() {
        return getAliveMafias().size();
    }

    /**
     * Citizen count int.
     *
     * @return the int
     */
    public int aliveCitizenCount() {
        return getAliveCitizens().size();
    }

    /**
     * Gets god father.
     *
     * @return the god father
     */
    public PlayerWorker getGodFather() {
        for (PlayerWorker playerWorker : getAliveMafias()) {
            if (playerWorker.getGameRoll().isGodFather()) {
                return playerWorker;
            }
        }

        return null;
    }

    /**
     * Gets doctor lector.
     *
     * @return the doctor lector
     */
    public PlayerWorker getDoctorLector() {
        for (PlayerWorker playerWorker : getAliveMafias()) {
            if (playerWorker.getGameRoll().isDoctorLector()) {
                return playerWorker;
            }
        }

        return null;
    }

    /**
     * Gets normal mafias.
     *
     * @return the normal mafias
     */
    public ArrayList<PlayerWorker> getNormalMafias() {
        ArrayList<PlayerWorker> normalMafias = new ArrayList<>();

        for (PlayerWorker playerWorker : getAliveMafias()) {
            if (playerWorker.getGameRoll().isNormalMafia()) {
                normalMafias.add(playerWorker);
            }
        }

        if (normalMafias.size() == 0) {
            return null;
        }

        return normalMafias;
    }

    /**
     * Gets mayor.
     *
     * @return the mayor
     */
    public PlayerWorker getMayor() {
        for (PlayerWorker playerWorker : getAliveCitizens()) {
            if (playerWorker.getGameRoll().isMayor()) return playerWorker;
        }

        return null;
    }

    /**
     * Gets city doctor.
     *
     * @return the city doctor
     */
    public PlayerWorker getCityDoctor() {
        for (PlayerWorker playerWorker : getAliveCitizens()) {
            if (playerWorker.getGameRoll().isCityDoctor()) return playerWorker;
        }

        return null;
    }

    /**
     * Gets mafia leader.
     *
     * @return the mafia leader
     */
    public PlayerWorker getMafiaLeader() {
        for (PlayerWorker mafiaWorker : getAliveMafias()) {
            Mafia mafia = (Mafia) mafiaWorker.getGameRoll();

            if (mafia.isLeader()) {
                return mafiaWorker;
            }

        }

        return null;
    }

    /**
     * Alive citizens to string string.
     *
     * @return the string
     */
    public String aliveCitizensToString() {
        StringBuilder builder = new StringBuilder();

        for (PlayerWorker citizen : getAliveCitizens()) {
            builder.append(citizen).append("\n");
        }

        return builder.toString();
    }

    /**
     * Sets new mafia leader.
     */
    public void setNewMafiaLeader() {
//        god father is dead have to select new mafia leader
//        the first priority is doctor lector
        PlayerWorker doctorLectorWorker = getDoctorLector();
        if (!Objects.isNull(doctorLectorWorker)) {
            Mafia doctorLector = (Mafia) doctorLectorWorker.getGameRoll();
            doctorLector.promoteToMafiaLeader();
            return;
        }

//        doctor lector and god father both are dead, selecting first normal mafia to be leader .
        PlayerWorker mafiaWorker = Objects.requireNonNull(getNormalMafias()).get(0);
        Mafia mafia = (Mafia) mafiaWorker.getGameRoll();
        mafia.promoteToMafiaLeader();
    }

    /**
     * Gets inspector.
     *
     * @return the inspector
     */
    public PlayerWorker getInspector() {
        for (PlayerWorker playerWorker : getAliveCitizens()) {
            if (playerWorker.getGameRoll().isInspector()) return playerWorker;
        }

        return null;
    }

    /**
     * Gets sniper.
     *
     * @return the sniper
     */
    public PlayerWorker getSniper() {

        for (PlayerWorker playerWorker : getAliveCitizens()) {
            if (playerWorker.getGameRoll().isSniper()) return playerWorker;
        }

        return null;
    }

    /**
     * Gets diehard.
     *
     * @return the diehard
     */
    public PlayerWorker getDiehard() {
        for (PlayerWorker playerWorker : getAliveCitizens()) {
            if (playerWorker.getGameRoll().isDieHard()) return playerWorker;
        }

        return null;
    }

    /**
     * Gets psychiatrist.
     *
     * @return the psychiatrist
     */
    public PlayerWorker getPsychiatrist() {
        for (PlayerWorker playerWorker : getAliveCitizens()) {
            if (playerWorker.getGameRoll().isPsychiatrist()) return playerWorker;
        }

        return null;
    }
}
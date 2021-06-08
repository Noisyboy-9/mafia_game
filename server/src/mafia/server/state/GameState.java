package mafia.server.state;

import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.enums.GameTimeEnum;
import mafia.server.exceptions.PlayerAlreadyExistException;
import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.workers.PlayerWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * The type Game state.
 */
public class GameState {
    private static GameState singletonInstance;
    private final ArrayList<PlayerWorker> alivePlayers;
    private final ArrayList<PlayerWorker> deadPlayers;
    private GameTimeEnum gameTimeEnum;

    private GameState() {
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
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
     * Go in night mode.
     */
    public void goInNightMode() {
        this.gameTimeEnum = GameTimeEnum.NIGHT;
    }

    /**
     * Go in day mode.
     */
    public void goInDayMode() {
        this.gameTimeEnum = GameTimeEnum.DAY;
    }

    /**
     * Go in poll mode.
     */
    public void goInPollMode() {
        this.gameTimeEnum = GameTimeEnum.POLL;
    }

    /**
     * Go in introduction night.
     */
    public void goInIntroductionNightMode() {
        this.gameTimeEnum = GameTimeEnum.INTRODUCTION_NIGHT;
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

        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
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

        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
            if (playerWorker.getGameRoll().isCitizen()) citizens.add(playerWorker);
        }

        return citizens;
    }

    /**
     * Gets singleton instance.
     *
     * @return the singleton instance
     */
    public static GameState getSingletonInstance() {
        if (Objects.isNull(singletonInstance)) {
            singletonInstance = new GameState();
        }

        return singletonInstance;
    }

    /**
     * Gets player worker by username.
     *
     * @param username the username
     * @return the player worker by username
     */
    public PlayerWorker getPlayerWorkerByUsername(String username) {
        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
            if (playerWorker.getUsername().equals(username)) {
                return playerWorker;
            }
        }

        return null;
    }

    /**
     * Username exist boolean.
     *
     * @param username the username
     * @return the boolean
     */
    public boolean playerWithUsernameExist(String username) {
        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
            if (playerWorker.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Alive players to string string.
     *
     * @return the string
     */
    public String alivePlayersToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("All Alive Players: \n");

        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
            builder.append(playerWorker.toString());
            builder.append("\n");
        }

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
     * Game report string string.
     *
     * @return the string
     */
    public String gameReportString() {
        if (getSingletonInstance().deadPlayers.isEmpty()) {
            return "no players all killed nothing to report about!";
        }

        StringBuilder builder = new StringBuilder();

        int killedMafiasCount = getSingletonInstance().countKilledMafias();
        int killCitizensCount = getSingletonInstance().countKilledCitizens();

        builder.append("killed mafias count: ").append(killedMafiasCount).append("\n");
        builder.append("killed citizens count: ").append(killCitizensCount).append("\n");

//        when informing every one that killed players had which rolls, it must be random and in no particular order.
        Collections.shuffle(getSingletonInstance().deadPlayers);


        if (killCitizensCount != 0) {
            HashSet<String> killedCitizenRoles = new HashSet<>();
            for (PlayerWorker playerWorker : getSingletonInstance().deadPlayers) {
                if (playerWorker.getGameRoll().isCitizen()) {
                    killedCitizenRoles.add(playerWorker.getGameRoll().getRollString());
                }
            }

            builder.append("killed citizens roles: \n");
            killedCitizenRoles.forEach(role -> builder.append(role).append("\n"));
        }

        if (killedMafiasCount != 0) {
            HashSet<String> killedMafiasRole = new HashSet<>();
            for (PlayerWorker playerWorker : getSingletonInstance().deadPlayers) {
                if (playerWorker.getGameRoll().isMafia()) {
                    killedMafiasRole.add(playerWorker.getGameRoll().getRollString());
                }
            }
            builder.append("killed mafias roles: \n");
            killedMafiasRole.forEach(role -> builder.append(role).append("\n"));
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
            builder.append(citizen);
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

    private int countKilledCitizens() {
        int counter = 0;

        for (PlayerWorker playerWorker : this.deadPlayers) {
            if (playerWorker.getGameRoll().isCitizen()) counter++;
        }

        return counter;
    }

    private int countKilledMafias() {
        int counter = 0;

        for (PlayerWorker playerWorker : this.deadPlayers) {
            if (playerWorker.getGameRoll().isMafia()) counter++;
        }

        return counter;
    }
}


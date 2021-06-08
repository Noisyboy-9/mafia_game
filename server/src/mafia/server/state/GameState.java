package mafia.server.state;

import mafia.server.exceptions.PlayerAlreadyExistException;
import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.state.managers.GameTimeManager;
import mafia.server.state.managers.PlayerWorkerManager;
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
    private final GameTimeManager gameTimeManager = new GameTimeManager();
    private final PlayerWorkerManager playerWorkerManager = new PlayerWorkerManager();

    private GameState() {
    }

    public ArrayList<PlayerWorker> getAlivePlayers() {
        return this.playerWorkerManager.getAlivePlayers();
    }

    /**
     * get all players in the game.
     *
     * @return the array list
     */
    public ArrayList<PlayerWorker> getAllGamePlayers() {
        return playerWorkerManager.getAllGamePlayers();
    }

    /**
     * Go in night mode.
     */
    public void goInNightMode() {
        gameTimeManager.goInNightMode();
    }

    /**
     * Go in day mode.
     */
    public void goInDayMode() {
        gameTimeManager.goInDayMode();
    }

    /**
     * Go in poll mode.
     */
    public void goInPollMode() {
        gameTimeManager.goInPollMode();
    }

    /**
     * Go in introduction night.
     */
    public void goInIntroductionNightMode() {
        gameTimeManager.goInIntroductionNightMode();
    }

    /**
     * Add player.
     *
     * @param playerWorker the player worker
     * @throws PlayerAlreadyExistException the player already exist exception
     */
    public void addPlayer(PlayerWorker playerWorker) throws PlayerAlreadyExistException {
        playerWorkerManager.addPlayer(playerWorker);
    }

    /**
     * Kill player.
     *
     * @param killTarget the kill target
     * @throws PlayerIsAlreadyDeadException the player is already dead exception
     */
    public void killPlayer(PlayerWorker killTarget) throws PlayerIsAlreadyDeadException {
        playerWorkerManager.killPlayer(killTarget);
    }

    /**
     * Gets alive mafias.
     *
     * @return the alive mafias
     */
    public ArrayList<PlayerWorker> getAliveMafias() {
        return playerWorkerManager.getAliveMafias();
    }

    /**
     * Gets alive citizens.
     *
     * @return the alive citizens
     */
    public ArrayList<PlayerWorker> getAliveCitizens() {
        return playerWorkerManager.getAliveCitizens();
    }

    /**
     * Gets player worker by username.
     *
     * @param username the username
     * @return the player worker by username
     */
    public PlayerWorker getPlayerWorkerByUsername(String username) {
        for (PlayerWorker playerWorker : getSingletonInstance().playerWorkerManager.getAlivePlayers()) {
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
        for (PlayerWorker playerWorker : getSingletonInstance().playerWorkerManager.getAlivePlayers()) {
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
        return playerWorkerManager.alivePlayersToString();
    }

    /**
     * Alive mafias to string string.
     *
     * @return the string
     */
    public String aliveMafiasToString() {
        return playerWorkerManager.aliveMafiasToString();
    }

    /**
     * Game report string string.
     *
     * @return the string
     */
    public String gameReportString() {
        if (getSingletonInstance().playerWorkerManager.getDeadPlayers().isEmpty()) {
            return "no players all killed nothing to report about!";
        }

        StringBuilder builder = new StringBuilder();

        int killedMafiasCount = getSingletonInstance().countKilledMafias();
        int killCitizensCount = getSingletonInstance().countKilledCitizens();

        builder.append("killed mafias count: ").append(killedMafiasCount).append("\n");
        builder.append("killed citizens count: ").append(killCitizensCount).append("\n");

//        when informing every one that killed players had which rolls, it must be random and in no particular order.
        Collections.shuffle(getSingletonInstance().playerWorkerManager.getDeadPlayers());


        if (killCitizensCount != 0) {
            HashSet<String> killedCitizenRoles = new HashSet<>();
            for (PlayerWorker playerWorker : getSingletonInstance().playerWorkerManager.getDeadPlayers()) {
                if (playerWorker.getGameRoll().isCitizen()) {
                    killedCitizenRoles.add(playerWorker.getGameRoll().getRollString());
                }
            }

            builder.append("killed citizens roles: \n");
            killedCitizenRoles.forEach(role -> builder.append(role).append("\n"));
        }

        if (killedMafiasCount != 0) {
            HashSet<String> killedMafiasRole = new HashSet<>();
            for (PlayerWorker playerWorker : getSingletonInstance().playerWorkerManager.getDeadPlayers()) {
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
        return playerWorkerManager.aliveMafiaCount();
    }

    /**
     * Citizen count int.
     *
     * @return the int
     */
    public int aliveCitizenCount() {
        return playerWorkerManager.aliveCitizenCount();
    }

    /**
     * Gets god father.
     *
     * @return the god father
     */
    public PlayerWorker getGodFather() {
        return playerWorkerManager.getGodFather();
    }

    /**
     * Gets doctor lector.
     *
     * @return the doctor lector
     */
    public PlayerWorker getDoctorLector() {
        return playerWorkerManager.getDoctorLector();
    }

    /**
     * Gets normal mafias.
     *
     * @return the normal mafias
     */
    public ArrayList<PlayerWorker> getNormalMafias() {
        return playerWorkerManager.getNormalMafias();
    }

    /**
     * Gets mayor.
     *
     * @return the mayor
     */
    public PlayerWorker getMayor() {
        return playerWorkerManager.getMayor();
    }

    /**
     * Gets city doctor.
     *
     * @return the city doctor
     */
    public PlayerWorker getCityDoctor() {
        return playerWorkerManager.getCityDoctor();
    }

    /**
     * Gets mafia leader.
     *
     * @return the mafia leader
     */
    public PlayerWorker getMafiaLeader() {
        return playerWorkerManager.getMafiaLeader();
    }

    /**
     * Alive citizens to string string.
     *
     * @return the string
     */
    public String aliveCitizensToString() {
        return playerWorkerManager.aliveCitizensToString();
    }

    /**
     * Sets new mafia leader.
     */
    public void setNewMafiaLeader() {
        playerWorkerManager.setNewMafiaLeader();
    }

    /**
     * Gets inspector.
     *
     * @return the inspector
     */
    public PlayerWorker getInspector() {
        return playerWorkerManager.getInspector();
    }

    /**
     * Gets sniper.
     *
     * @return the sniper
     */
    public PlayerWorker getSniper() {
        return playerWorkerManager.getSniper();
    }

    /**
     * Gets diehard.
     *
     * @return the diehard
     */
    public PlayerWorker getDiehard() {
        return playerWorkerManager.getDiehard();
    }

    /**
     * Gets psychiatrist.
     *
     * @return the psychiatrist
     */
    public PlayerWorker getPsychiatrist() {
        return playerWorkerManager.getPsychiatrist();
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

    private int countKilledCitizens() {
        int counter = 0;

        for (PlayerWorker playerWorker : this.playerWorkerManager.getDeadPlayers()) {
            if (playerWorker.getGameRoll().isCitizen()) counter++;
        }

        return counter;
    }

    private int countKilledMafias() {
        int counter = 0;

        for (PlayerWorker playerWorker : this.playerWorkerManager.getDeadPlayers()) {
            if (playerWorker.getGameRoll().isMafia()) counter++;
        }

        return counter;
    }
}


package mafia.server.state;

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
    public static PlayerWorker getPlayerWorkerByUsername(String username) {
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
    public static boolean usernameExist(String username) {
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
    public static String alivePlayersToString() {
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
    public static String aliveMafiasToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("All Alive Mafias: \n");

        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
            if (playerWorker.getGameRoll().isMafia()) {
                builder.append(playerWorker);
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    /**
     * Game report string string.
     *
     * @return the string
     */
    public static String gameReportString() {
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

        this.alivePlayers.remove(killTarget);
        this.deadPlayers.add(killTarget);
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

    private GameState() {
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
    }


}


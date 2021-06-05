package mafia.server.state;

import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.exceptions.SettingAlivePlayersInMiddleOfGameException;
import mafia.server.workers.PlayerWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class GameState {
    private static GameState singletonInstance;
    private final ArrayList<PlayerWorker> alivePlayers = new ArrayList<>();
    private final ArrayList<PlayerWorker> deadPlayers = new ArrayList<>();

    public static GameState getSingletonInstance() {
        if (Objects.isNull(singletonInstance)) {
            singletonInstance = new GameState();
        }

        return singletonInstance;
    }

    public static PlayerWorker getPlayerWorkerByUsername(String username) {
        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
            if (playerWorker.getUsername().equals(username)) {
                return playerWorker;
            }
        }

        return null;
    }

    public static boolean usernameExist(String username) {
        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
            if (playerWorker.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public static String alivePlayersToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("All Alive Players: \n");

        for (PlayerWorker playerWorker : getSingletonInstance().alivePlayers) {
            builder.append(playerWorker.toString());
            builder.append("\n");
        }

        return builder.toString();
    }

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

    public static String gameReportString() {
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


    public void setAlivePlayers(ArrayList<PlayerWorker> players) throws SettingAlivePlayersInMiddleOfGameException {
        if (!this.alivePlayers.isEmpty()) {
            throw new SettingAlivePlayersInMiddleOfGameException("Game has alive players can't set them in the middle of game");
        }

        this.alivePlayers.addAll(players);
    }

    public void killPlayer(PlayerWorker killTarget) throws PlayerIsAlreadyDeadException {
        if (this.deadPlayers.contains(killTarget)) {
            throw new PlayerIsAlreadyDeadException("Player has been already killed can't kill him twice!");
        }

        this.alivePlayers.remove(killTarget);
        this.deadPlayers.add(killTarget);
    }


}


package mafia.server.manager;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GameManager {
    public void handleIntroductionNight() {
        this.introduceMafias();
        this.introduceCityDoctorToMayor();
    }

    private void introduceCityDoctorToMayor() {
        PlayerWorker mayor = GameState.getMayor();
        PlayerWorker cityDoctor = GameState.getCityDoctor();

        if (mayor != null && cityDoctor != null) {
            ObjectOutputStream mayorResponse = mayor.getResponse();
            try {
                mayorResponse.writeObject(new ShowMessageCommand(cityDoctor.getUsername() + " is city doctor"));
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(mayor);
            }
        }
    }

    private void introduceMafias() {
        ArrayList<PlayerWorker> mafias = GameState.getAliveMafias();
        String mafiaString = this.generateMafiaIntroductionString();

        for (PlayerWorker mafia : mafias) {
            ObjectOutputStream response = mafia.getResponse();
            try {
                response.writeObject(new ShowMessageCommand(mafiaString));
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(mafia);
            }
        }
    }

    private void handlePlayerDisconnect(PlayerWorker playerWorker) {
        this.broadcastMessageToAll(playerWorker.getUsername());
        try {
            GameState.getSingletonInstance().killPlayer(playerWorker);
        } catch (PlayerIsAlreadyDeadException ignored) {
        }
    }

    private void broadcastMessageToAll(String message) {
        ArrayList<PlayerWorker> playerWorkers = GameState.getSingletonInstance().getAllGamePlayers();
        for (PlayerWorker playerWorker : playerWorkers) {
            ObjectOutputStream response = playerWorker.getResponse();

            try {
                response.writeObject(new ShowMessageCommand(message));
            } catch (IOException ignored) {
            }
        }
    }

    private String generateMafiaIntroductionString() {
        StringBuilder builder = new StringBuilder();

        PlayerWorker godFather = GameState.getGodFather();
        PlayerWorker doctorLector = GameState.getDoctorLector();
        ArrayList<PlayerWorker> normalMafias = GameState.getNormalMafias();


        if (godFather != null) {
            builder.append(godFather.getUsername()).append(" is god father.").append("\n");
        }

        if (doctorLector != null) {
            builder.append(doctorLector.getUsername()).append(" is doctor lector.").append("\n");
        }

        if (normalMafias != null) {
            for (PlayerWorker normalMafia : normalMafias) {
                builder.append(normalMafia.getUsername()).append(" is normal mafia.").append("\n");
            }
        }

        return builder.toString();
    }
}
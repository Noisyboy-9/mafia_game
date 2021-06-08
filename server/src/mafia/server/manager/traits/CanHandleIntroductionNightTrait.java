package mafia.server.manager.traits;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * The interface Can handle introduction night trait.
 */
public interface CanHandleIntroductionNightTrait extends CanHandlePlayerDisconnect {
    /**
     * Handle introduction night.
     */
    default void handleIntroductionNight() {
        this.introduceMafias();
        this.introduceCityDoctorToMayor();
    }

    private void introduceCityDoctorToMayor() {
        PlayerWorker mayor = GameState.getSingletonInstance().getMayor();
        PlayerWorker cityDoctor = GameState.getSingletonInstance().getCityDoctor();

        if (mayor != null && cityDoctor != null) {
            ObjectOutputStream mayorResponse = mayor.getResponse();
            try {
                mayorResponse.writeObject(new ShowMessageCommand(cityDoctor.getUsername() + " is city doctor").toString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(mayor);
            }
        }
    }

    private void introduceMafias() {
        ArrayList<PlayerWorker> mafias = GameState.getSingletonInstance().getAllGamePlayers();
        String mafiaString = this.generateMafiaIntroductionString();

        for (PlayerWorker mafia : mafias) {
            ObjectOutputStream response = mafia.getResponse();
            try {
                response.writeObject(new ShowMessageCommand(mafiaString).toString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(mafia);
            }
        }
    }

    private String generateMafiaIntroductionString() {
        StringBuilder builder = new StringBuilder();

        PlayerWorker godFather = GameState.getSingletonInstance().getGodFather();
        PlayerWorker doctorLector = GameState.getSingletonInstance().getDoctorLector();
        ArrayList<PlayerWorker> normalMafias = GameState.getSingletonInstance().getNormalMafias();


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

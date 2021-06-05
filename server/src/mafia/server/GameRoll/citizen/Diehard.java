package mafia.server.GameRoll.citizen;

import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.commands.GetInputCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Diehard extends Citizen {
    private int remainingReportCount;
    private boolean hasArmor;

    public Diehard() {
        this.remainingReportCount = 3;
        this.hasArmor = true;
    }

    public boolean askForGameReport(PlayerWorker dieHard) {
        if (this.remainingReportCount == 0) {
//            die hard can not get report more than default count of report times.
            return false;
        }

        ObjectOutputStream response = dieHard.getResponse();

        try {
            response.writeObject(new GetInputCommand("Want to get killed players report?(Y for yes, N for no)").toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


        ObjectInputStream request = dieHard.getRequest();
        String dieHardResponse;
        try {
            dieHardResponse = (String) request.readObject();
            return dieHardResponse.equalsIgnoreCase("y");
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }

    public String getGameReport() {
        this.remainingReportCount--;
        return GameState.makeGameReportString();
    }

    public boolean hasArmor() {
        return this.hasArmor;
    }

    public void removeArmor() {
        this.hasArmor = false;
    }
}

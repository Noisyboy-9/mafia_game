package mafia.server.GameRoll.citizen;

import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.commands.GetInputCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The type Diehard.
 */
public class Diehard extends Citizen {
    private int remainingReportCount;
    private boolean hasArmor;

    /**
     * Ask for game report boolean.
     *
     * @param dieHard the die hard
     * @return the boolean
     */
    public boolean wantsGameReport(PlayerWorker dieHard) {
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

    /**
     * Gets game report.
     *
     * @return the game report
     */
    public String getGameReportString() {
        this.remainingReportCount--;
        return GameState.getSingletonInstance().gameReportString();
    }

    public void sendGameReportString(PlayerWorker diehard, String report) throws IOException {
        ObjectOutputStream response = diehard.getResponse();
        response.writeObject(new ShowMessageCommand(report).toString());
    }

    /**
     * Has armor boolean.
     *
     * @return the boolean
     */
    public boolean hasArmor() {
        return this.hasArmor;
    }

    /**
     * Remove armor.
     */
    public void removeArmor() {
        this.hasArmor = false;
    }

    /**
     * Instantiates a new Diehard.
     */
    public Diehard() {
        this.remainingReportCount = 3;
        this.hasArmor = true;
    }
}

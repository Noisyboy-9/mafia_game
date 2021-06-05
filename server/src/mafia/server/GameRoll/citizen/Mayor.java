package mafia.server.GameRoll.citizen;

import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.commands.GetInputCommand;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The type Mayor.
 */
public class Mayor extends Citizen {
    /**
     * Ask for poll cancellation boolean.
     *
     * @param mayor the mayor
     * @return the boolean
     */
    public boolean askForPollCancellation(PlayerWorker mayor) {
        ObjectOutputStream response = mayor.getResponse();

        try {
            response.writeObject(new GetInputCommand("want to cancel poll?(Y for yes, N for no)"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        ObjectInputStream request = mayor.getRequest();
        try {
            String mayorDecision = (String) request.readObject();
            return mayorDecision.equalsIgnoreCase("y");
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }
}

package mafia.server.GameRoll.citizen;

import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.GameRoll.traits.CanSeeAllPlayersTrait;
import mafia.server.GameRoll.traits.CanSelectPlayerTrait;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The type Inspector.
 */
public class Inspector extends Citizen implements CanSeeAllPlayersTrait, CanSelectPlayerTrait {
    /**
     * Gets player report.
     *
     * @param inspector the inspector
     */
    public void getPlayerReport(PlayerWorker inspector) {
        this.showAllPlayersToClient(inspector);
        PlayerWorker reportTarget = this.getSelectedPlayer(inspector);
        this.sendReport(reportTarget.getGameRoll().isMafia(), inspector, reportTarget);
    }

    private void sendReport(boolean isMafia, PlayerWorker inspector, PlayerWorker reportTarget) {
        ObjectOutputStream response = inspector.getResponse();

        String reportMessage = isMafia ?
                reportTarget.getUsername() + "is Mafia!" :
                reportTarget.getUsername() + "is not mafia";

        try {
            response.writeObject(new ShowMessageCommand(reportMessage).toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

package mafia.server.player.mafia.abstacts;

import mafia.server.player.Player;
import mafia.server.player.citizen.Citizen;

/**
 * Mafia role of the game.
 */
public abstract class Mafia extends Player {
    private boolean isLeader = false;

    /**
     * Select citizen to kill.
     *
     * @param citizen the citizen
     */
    public void selectCitizenToKill(Citizen citizen) {
        citizen.kill();
    }

    public void makeLeader() {
        this.isLeader = true;
    }
}

package mafia.server.GameRoll.mafia.abstacts;

import mafia.server.GameRoll.GameRoll;
import mafia.server.GameRoll.citizen.abstracts.Citizen;

/**
 * Mafia role of the game.
 */
public abstract class Mafia extends GameRoll {
    protected boolean isLeader = false;

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

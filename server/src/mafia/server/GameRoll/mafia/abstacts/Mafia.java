package mafia.server.GameRoll.mafia.abstacts;

import mafia.server.GameRoll.GameRoll;
import mafia.server.GameRoll.citizen.abstracts.Citizen;

/**
 * Mafia role of the game.
 */
public abstract class Mafia extends GameRoll {
    /**
     * The Is leader.
     */
    protected boolean isLeader = false;

    /**
     * Select citizen to kill.
     *
     * @param citizen the citizen
     */
    public void selectCitizenToKill(Citizen citizen) {
        citizen.kill();
    }

    /**
     * Make this the leader of mafias.
     */
    public void promoteToMafiaLeader() {
        this.isLeader = true;
    }
}

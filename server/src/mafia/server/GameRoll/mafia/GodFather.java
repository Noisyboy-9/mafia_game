package mafia.server.GameRoll.mafia;

import mafia.server.GameRoll.mafia.abstacts.Mafia;

/**
 * The role God Father in mafia game
 */
public class GodFather extends Mafia {
    /**
     * Instantiates a new God father and set it to be the mafias leader.
     */
    public GodFather() {
//        god father is the leader of mafia's at the game start.
//        he will be leader until he get killed.
        this.makeLeader();
    }

    @Override
    public void kill() {
        this.isLeader = false;
        super.kill();
    }
}

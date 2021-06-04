package mafia.server.player.mafia;

import mafia.server.player.mafia.abstacts.Mafia;

public class GodFather extends Mafia {
    public GodFather() {
//        god father is the leader of mafia's at the game start.
//        he will be leader until he get killed.
        this.makeLeader();
    }
}

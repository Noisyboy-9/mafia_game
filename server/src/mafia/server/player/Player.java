package mafia.server.player;

import mafia.server.player.mafia.abstacts.Mafia;

public abstract class Player {
    private boolean speakPermission;
    private int id;
    private boolean isKilled;
    private boolean isServerAdmin;

    public void setSpeakPermission(boolean speakPermission) {
        this.speakPermission = speakPermission;
    }


    public boolean isMafia() {
        return this instanceof Mafia;
    }

    public boolean haveSpeakPermission() {
        return this.speakPermission;
    }

    public boolean isServerAdmin() {
        return this.isServerAdmin;
    }

    public void setServerAdmin(boolean serverAdmin) {
        this.isServerAdmin = serverAdmin;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isKilled() {
        return this.isKilled;
    }

    public void kill() {
        this.isKilled = true;
    }

    public void revive() {
        if (this.isKilled) {
            this.isKilled = false;
        }
    }
}

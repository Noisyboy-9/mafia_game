package mafia.server.player;

public abstract class Player {
    private boolean speakPermission;
    private int id;
    private boolean isKilled;
    private boolean isServerAdmin;

    public void setSpeakPermission(boolean speakPermission) {
        this.speakPermission = speakPermission;
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

    public void kill(boolean killed) {
        this.isKilled = killed;
    }
}

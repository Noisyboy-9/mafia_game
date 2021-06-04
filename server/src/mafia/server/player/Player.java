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
        return speakPermission;
    }

    public boolean isServerAdmin() {
        return this.isServerAdmin;
    }

    public void setServerAdmin(boolean serverAdmin) {
        isServerAdmin = serverAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isKilled() {
        return this.isKilled;
    }

    public void setKilled(boolean killed) {
        isKilled = killed;
    }
}

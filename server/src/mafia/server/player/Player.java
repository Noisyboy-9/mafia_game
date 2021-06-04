package mafia.server.player;

import mafia.server.player.mafia.abstacts.Mafia;

/**
 * The abstract type player in mafia game.
 */
public abstract class Player {
    private boolean speakPermission;
    private int id;
    private boolean isKilled;
    private boolean isServerAdmin;

    /**
     * Sets speak permission.
     *
     * @param speakPermission the speak permission
     */
    public void setSpeakPermission(boolean speakPermission) {
        this.speakPermission = speakPermission;
    }


    /**
     * Check if player is mafia.
     *
     * @return the boolean
     */
    public boolean isMafia() {
        return this instanceof Mafia;
    }

    /**
     * Have speak permission.
     *
     * @return the boolean
     */
    public boolean haveSpeakPermission() {
        return this.speakPermission;
    }

    /**
     * Is the current player server admin.
     *
     * @return the boolean
     */
    public boolean isServerAdmin() {
        return this.isServerAdmin;
    }

    /**
     * Sets server admin.
     */
    public void promoteToServerAdmin() {
        this.isServerAdmin = true;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * check if player is killed.
     *
     * @return the boolean
     */
    public boolean isKilled() {
        return this.isKilled;
    }

    /**
     * Kill the player.
     */
    public void kill() {
        this.isKilled = true;
    }

    /**
     * Revive the player if it is killed.
     */
    public void revive() {
        if (this.isKilled) {
            this.isKilled = false;
        }
    }
}

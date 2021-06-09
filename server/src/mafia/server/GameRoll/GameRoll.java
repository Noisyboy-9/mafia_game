package mafia.server.GameRoll;

import mafia.server.GameRoll.traits.CanKnowItsExactRollTrait;
import mafia.server.GameRoll.traits.CanParticipateInPollTrait;

/**
 * The abstract type player in mafia game.
 */
public abstract class GameRoll implements CanKnowItsExactRollTrait, CanParticipateInPollTrait {
    private boolean speakPermission;
    private int id;
    private boolean isKilled;
    private boolean isServerAdmin;

    /**
     * mute player so it can't speak in the chat for the next hand.
     *
     * @param speakPermission the speak permission
     */
    public void mute() {
        this.speakPermission = false;
    }

    /**
     * Unmute player so it can talk in the chat.
     */
    public void unmute() {
        this.speakPermission = true;
    }

    /**
     * Have speak permission.
     *
     * @return the boolean
     */
    public boolean canSpeak() {
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

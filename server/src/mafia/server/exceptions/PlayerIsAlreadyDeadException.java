package mafia.server.exceptions;

/**
 * The type Player is already dead exception.
 */
public class PlayerIsAlreadyDeadException extends Exception {
    /**
     * Instantiates a new Player is already dead exception.
     *
     * @param message the message
     */
    public PlayerIsAlreadyDeadException(String message) {
        super(message);
    }
}

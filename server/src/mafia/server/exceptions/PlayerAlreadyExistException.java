package mafia.server.exceptions;

/**
 * The type Player already exist exception.
 */
public class PlayerAlreadyExistException extends Exception {
    /**
     * Instantiates a new Player already exist exception.
     *
     * @param message the message
     */
    public PlayerAlreadyExistException(String message) {
        super(message);
    }
}

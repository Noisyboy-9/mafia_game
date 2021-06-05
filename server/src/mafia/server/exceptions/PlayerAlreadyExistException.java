package mafia.server.exceptions;

public class PlayerAlreadyExistException extends Exception {
    public PlayerAlreadyExistException(String message) {
        super(message);
    }
}

package mafia.server.exceptions;

public class PlayerIsAlreadyDeadException extends Exception {
    public PlayerIsAlreadyDeadException(String message) {
        super(message);
    }
}

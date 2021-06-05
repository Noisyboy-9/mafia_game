package mafia.server.commands;

/**
 * Show message command.
 */
public class ShowMessageCommand implements Command {
    private final String message;

    @Override
    public String toString() {
        return "showMessage" + " " + this.message;
    }

    /**
     * Instantiates a new Show message command.
     *
     * @param message the message
     */
    public ShowMessageCommand(String message) {
        this.message = message;
    }
}

package mafia.server.commands;

public class ShowMessageCommand implements Command {
    private final String message;

    public ShowMessageCommand(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return "showMessage" + " " + this.message;
    }
}

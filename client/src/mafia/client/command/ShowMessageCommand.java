package mafia.client.command;

public class ShowMessageCommand extends Command {
    private final String message;

    public ShowMessageCommand(String commandString) {
        super(commandString);
        this.message = commandString.split("//s+")[1];
    }

    @Override
    public void handle() {
        System.out.println(message);
    }
}

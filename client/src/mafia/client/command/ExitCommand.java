package mafia.client.command;

public class ExitCommand extends Command {
    public ExitCommand(String commandString) {
        super(commandString);
    }

    @Override
    public void handle() {
        new ClientCloser().close();
    }
}

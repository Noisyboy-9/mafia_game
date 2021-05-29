package mafia.client.command;

public abstract class Command {
    private String[] tokens = null;

    public Command(String commandString) {
        this.tokens = commandString.split("//s+");
    }

    public String[] getTokens() {
        return tokens;
    }

    public abstract void handle();
}

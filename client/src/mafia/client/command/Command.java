package mafia.client.command;

import java.io.Serializable;

public abstract class Command implements Serializable {
    protected final String[] tokens;

    public Command(String commandString) {
        this.tokens = commandString.split(" ");
    }

    public abstract void handle();
}

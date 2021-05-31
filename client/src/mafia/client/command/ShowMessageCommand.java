package mafia.client.command;

import java.util.ArrayList;
import java.util.Arrays;

public class ShowMessageCommand extends Command {
    private final ArrayList<String> messages = new ArrayList<>();

    public ShowMessageCommand(String commandString) {
        super(commandString);
        this.messages.addAll(Arrays.asList(tokens).subList(1, tokens.length));
    }

    @Override
    public void handle() {
        System.out.println(String.join(" ", this.messages));
    }
}

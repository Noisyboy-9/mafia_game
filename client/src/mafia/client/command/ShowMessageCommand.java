package mafia.client.command;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Show message command.
 */
public class ShowMessageCommand extends Command {
    private final ArrayList<String> messages = new ArrayList<>();

    /**
     * Instantiates a new Show message command.
     *
     * @param commandString the command string
     */
    public ShowMessageCommand(String commandString) {
        super(commandString);
        this.messages.addAll(Arrays.asList(tokens).subList(1, tokens.length));
    }

    @Override
    public void handle() {
        System.out.println(String.join(" ", this.messages));
    }
}

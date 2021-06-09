package mafia.client.command;

import mafia.client.runnables.GetUserMessageRunnable;

import java.io.ObjectOutputStream;

/**
 * The type Start chat command.
 */
public class StartChatCommand extends Command {
    private final ObjectOutputStream request;

    /**
     * Instantiates a new Start chat command.
     *
     * @param tokens the tokens
     */
    public StartChatCommand(String tokens, ObjectOutputStream request) {
        super(tokens);
        this.request = request;
    }

    @Override
    public void handle() {
        Thread messageGetter = new Thread(new GetUserMessageRunnable(this.request));
        messageGetter.start();
    }
}

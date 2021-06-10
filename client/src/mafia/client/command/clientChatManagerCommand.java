package mafia.client.command;

import mafia.client.runnables.GetUserMessageRunnable;

import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Start chat command.
 */
public class clientChatManagerCommand extends Command {
    private final ObjectOutputStream request;
    private ExecutorService executorService;

    /**
     * Instantiates a new Start chat command.
     *
     * @param tokens the tokens
     */
    public clientChatManagerCommand(String tokens, ObjectOutputStream request) {
        super(tokens);
        this.request = request;
    }

    @Override
    public void handle() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new GetUserMessageRunnable(request));
        this.executorService = executorService;
    }

    public void close() {
        this.executorService.shutdownNow();
    }
}

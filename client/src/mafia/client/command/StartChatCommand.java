package mafia.client.command;

import mafia.client.runnables.GetUserMessageRunnable;

import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new GetUserMessageRunnable(request));
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

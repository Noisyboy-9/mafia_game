package mafia.client.runnables;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * The type Write chat history to console runnable.
 */
public class WriteChatHistoryToConsoleRunnable implements Runnable {
    private final ObjectInputStream response;
    private final Thread userInputGetterThread;

    /**
     * Instantiates a new Write chat history to console runnable.
     *
     * @param response              the response
     * @param userInputGetterThread the user input getter thread
     */
    public WriteChatHistoryToConsoleRunnable(ObjectInputStream response, Thread userInputGetterThread) {
        this.response = response;
        this.userInputGetterThread = userInputGetterThread;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String serverResponse = (String) this.response.readObject();

                if (serverResponse.equals("closeChat")) {
                    userInputGetterThread.interrupt();
                    break;
                }

                System.out.println(serverResponse);
            }
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("problem is in the reader");
        }
    }
}

package mafia.client.runnables;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * The type Get user message runnable.
 */
public class GetUserMessageRunnable implements Runnable {
    private final ObjectOutputStream request;

    /**
     * Instantiates a new Get user message runnable.
     *
     * @param request the request
     */
    public GetUserMessageRunnable(ObjectOutputStream request) {
        this.request = request;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String userMessage = this.getUserMessage();

                this.sendMessageToServer(userMessage);

                if (userMessage.equalsIgnoreCase("ready")) {
                    System.out.println("ready command received");
                    System.out.println("Waiting for other players to ready up!");
                    break;
                }
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                System.out.println("Client connection to chat server lost");
                break;
            }
        }
    }

    private void sendMessageToServer(String userMessage) throws IOException {
        this.request.writeObject(userMessage);
    }

    private String getUserMessage() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }
}

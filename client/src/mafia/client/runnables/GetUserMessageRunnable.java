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
                this.sendMessageToServer(this.getUserMessage());
                Thread.sleep(10_000);
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
                System.out.println("Client connection to chat server lost");
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToServer(String userMessage) throws IOException {
        this.request.writeObject(userMessage);
    }

    private String getUserMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input your message:\t");
        return scanner.nextLine();
    }
}

package mafia.client.closer;

import java.io.IOException;
import java.net.Socket;

/**
 * client socket closer.
 */
public class ClientCloser {
    /**
     * Close client and it's socket.
     *
     * @param socket the socket
     */
    public void close(Socket socket) {
        try {
            socket.close();
            System.out.println("Hope you have enjoyed 😊");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

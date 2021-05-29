package mafia.client.manager;

import mafia.client.command.GetUserInputCommand;
import mafia.client.command.ShowMessageCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * The type Client manager.
 */
public class ClientManager {
    private ObjectInputStream response;

    /**
     * Instantiates a new Client manager.
     *
     * @param socket the socket
     */
    public ClientManager(Socket socket) {
        try {
            this.response = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Handle commands that are issued by the socket server.
     */
    public void handleServerCommand() {
        try {
            String tokens = (String) this.response.readObject();
            String command = tokens.split("//s+")[0];


            while (!command.equals("exit")) {
                if (command.equals("getInput")) new GetUserInputCommand(command).handle();
                if (command.equals("showMessage")) new ShowMessageCommand(command).handle();

                tokens = (String) this.response.readObject();
                command = tokens.split("//s+")[0];
            }
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }
}

package mafia.client.manager;

import mafia.client.command.GetInputCommand;
import mafia.client.command.ShowMessageCommand;
import mafia.client.command.StartChatCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The type Client manager.
 */
public class ClientManager {
    private final ObjectInputStream response;
    private final ObjectOutputStream request;

    /**
     * Instantiates a new Client manager.
     *
     * @param response the response
     * @param request  the request
     */
    public ClientManager(ObjectInputStream response, ObjectOutputStream request) {
        this.response = response;
        this.request = request;
    }

    /**
     * Handle commands that are issued by the socket server.
     */
    public void handleServerCommands() {
        try {
            String tokens = (String) this.response.readObject();
            String command = tokens.split(" ")[0];


            while (!command.equals("exit")) {
                if (command.equals("getInput")) new GetInputCommand(tokens, request).handle();
                if (command.equals("showMessage")) new ShowMessageCommand(tokens).handle();
                if (command.equals("startChat")) new StartChatCommand(tokens, request).handle();
                if (command.equals("killClient")) {
                    System.out.println("you have been killed");
                    break;
                }
                tokens = (String) this.response.readObject();
                command = tokens.split(" ")[0];
            }
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("something went wrong");
            System.out.println(ioException.getMessage());
        }
    }
}

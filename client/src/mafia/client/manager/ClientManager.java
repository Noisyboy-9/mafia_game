package mafia.client.manager;

import mafia.client.command.GetInputCommand;
import mafia.client.command.ShowMessageCommand;
import mafia.client.command.clientChatManagerCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Scanner;

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


            clientChatManagerCommand chatManager = null;
            while (!command.equals("exit")) {
                if (command.equals("getInput")) {
                    new GetInputCommand(tokens, request).handle();
                }

                if (command.equals("showMessage")) {
                    new ShowMessageCommand(tokens).handle();
                }

                if (command.equals("startChat")) {
                    chatManager = new clientChatManagerCommand(tokens, request);
                    chatManager.handle();
                }

                if (command.equals("closeChat")) {
                    Objects.requireNonNull(chatManager).close();
                }

                if (command.equals("killClient")) {
                    if (!this.clientWantsToGoInSpectatorMode()) {
                        break;
                    }
                }

                tokens = (String) this.response.readObject();
                command = tokens.split(" ")[0];
            }
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private boolean clientWantsToGoInSpectatorMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Want to go in spectator mode(Y for yes, N for no): ");
        String input = scanner.nextLine();

        return input.equalsIgnoreCase("y");
    }
}

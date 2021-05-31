package mafia.client.starter;

import mafia.client.closer.ClientCloser;
import mafia.client.manager.ClientManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Start a client for the game.
 */
public class ClientStarter {
    private String gamePort;
    private Socket socket = null;
    private ObjectOutputStream request;
    private ObjectInputStream response;

    /**
     * Instantiates a new Client starter.
     */
    public ClientStarter() {
        this.getInputPort();
        this.setSocket();
        this.startClient();
    }

    private void setSocket() {
        try {
            this.socket = new Socket("127.0.0.1", Integer.parseInt(this.gamePort));
            this.request = new ObjectOutputStream(this.socket.getOutputStream());
            this.response = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getInputPort() {
        System.out.print("When you are ready, please input game port: ");
        Scanner scanner = new Scanner(System.in);
        this.gamePort = scanner.nextLine();
    }

    private String getInputUsername() {
        System.out.print("please input your username: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void startClient() {
        String username = this.getInputUsername();

//        check if another client with the same username has already connected to server
        while (this.usernameAlreadyExist(username)) {
            System.out.println("username already exist!");
            username = this.getInputUsername();
        }

        this.startClientManager();
    }

    private void startClientManager() {
        new ClientManager(this.response, this.request).handleServerCommands();


//        client work is finished
        new ClientCloser().close(socket);
    }

    private boolean usernameAlreadyExist(String username) {
        try {
            this.request.writeObject(username);
            return (Boolean) response.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }
}

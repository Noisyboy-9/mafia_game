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

    /**
     * Instantiates a new Client starter.
     */
    public ClientStarter() {
        this.getInputPort();
        this.setSocket();
        this.startClient();
    }

    private void setSocket() {
        try (Socket socket = new Socket("127.0.0.1", Integer.parseInt(this.gamePort))) {
            this.socket = socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void askForReady() {
        System.out.println("please type READY as soon as you ready...");
        Scanner scanner = new Scanner(System.in);

//        getting all inputs and ignoring ones that are not READY
        String input = scanner.nextLine();
        while (!input.equals("READY")) {
            input = scanner.nextLine();
        }
    }

    private void getInputPort() {
        System.out.print("please input game port: ");
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

        this.askForReady();
        this.startClientManager();
    }

    private void startClientManager() {
        new ClientManager(this.socket).handleServerCommand();

//        client work is finished
        new ClientCloser().close(socket);
    }

    private boolean usernameAlreadyExist(String username) {
        try {
            ObjectInputStream response = new ObjectInputStream(this.socket.getInputStream());
            ObjectOutputStream request = new ObjectOutputStream(this.socket.getOutputStream());

            request.writeObject(username);
            return (Boolean) response.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }
}

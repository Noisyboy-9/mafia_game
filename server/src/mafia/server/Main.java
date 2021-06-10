package mafia.server;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.exceptions.InvalidMaxPlayerCountException;
import mafia.server.exceptions.NotEnoughPlayerException;
import mafia.server.starter.GameStarter;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        String serverPort = "8080";
        System.out.println("Server is running on port: " + serverPort);
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(serverPort))) {
//            get ready for starting the game.
            GameStarter startManager = new GameStarter();
            while (!startManager.isServerFull()) {
                waitForPlayerToJoin(serverSocket, startManager);
            }

            startManager.startNewGame();
        } catch (IOException | ClassNotFoundException | NotEnoughPlayerException | InvalidMaxPlayerCountException exception) {
            exception.printStackTrace();
        }
    }

    private static void waitForPlayerToJoin(ServerSocket serverSocket, GameStarter starter) throws IOException, ClassNotFoundException {
//             accept socket connection
        Socket socket = serverSocket.accept();
        System.out.println("player connection accepted");
        ObjectOutputStream response = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream request = new ObjectInputStream(socket.getInputStream());

//            input username from the client
        String inputUsername = (String) request.readObject();

//            make sure input player chosen username is unique.
        while (starter.usernameExist(inputUsername)) {
            response.writeObject(true);
            inputUsername = (String) request.readObject();
        }

        response.writeObject(false);
//            we are sure input username is unique
        response.writeObject(new ShowMessageCommand("connected!").toString());
        starter.addPlayer(new PlayerWorker(socket, request, response, inputUsername));
        starter.sendPlayerConnectedMessageToOthers();
    }
}

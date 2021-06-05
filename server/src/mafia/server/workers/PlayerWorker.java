package mafia.server.workers;

import mafia.server.GameRoll.GameRoll;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Player worker which is a representative of a client in server side.
 * each time the server wants to access the client it will do so by it's worker.
 */
public class PlayerWorker extends Thread {
    private final Socket socket;
    private final ObjectOutputStream response;
    private final ObjectInputStream request;
    private final String username;
    private GameRoll gameRoll;

    /**
     * Instantiates a new Player worker.
     *
     * @param socket   the socket
     * @param request  the request
     * @param response the response
     * @param username the username
     */
    public PlayerWorker(Socket socket, ObjectInputStream request, ObjectOutputStream response, String username) {
        this.socket = socket;
        this.request = request;
        this.response = response;
        this.username = username;
        this.setName(username);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlayerWorker that = (PlayerWorker) object;
        return this.username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Get response stream from server to client.
     *
     * @return the response
     */
    public ObjectOutputStream getResponse() {
        return response;
    }

    /**
     * Get request stream from client to server.
     *
     * @return the request
     */
    public ObjectInputStream getRequest() {
        return request;
    }

    /**
     * Get the socket between server and client.
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Get client role in the game and it's status.
     *
     * @return the player
     */
    public GameRoll getGameRoll() {
        return this.gameRoll;
    }

    /**
     * Get client username in the server.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "id: " + this.getGameRoll().getId() + " username: " + this.getUsername();
    }
}

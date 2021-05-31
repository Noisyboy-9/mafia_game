package mafia.server.workers;

import mafia.server.player.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class PlayerWorker extends Thread {
    private final Socket socket;
    private final ObjectOutputStream response;
    private final ObjectInputStream request;
    private final String username;
    private Player player;

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

    public ObjectOutputStream getResponse() {
        return response;
    }

    public ObjectInputStream getRequest() {
        return request;
    }

    public Socket getSocket() {
        return socket;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getUsername() {
        return username;
    }
}

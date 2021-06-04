package mafia.server.starter;

import mafia.server.commands.GetInputCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.exceptions.NotEnoughPlayerException;
import mafia.server.loop.GameLoop;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The starter of the game, which handles players being added to the server and starting the game loop.
 * it also will set the GameState before the game start.
 */
public class GameStarter {
    private final ArrayList<PlayerWorker> players = new ArrayList<>();
    private final int maxPlayerCount = 10;
    private PlayerWorker serverAdmin = null;
//    private GameState gameState;

    /**
     * Check if already have a worker with same username.
     *
     * @param username the username
     * @return the boolean
     */
    public boolean usernameExist(String username) {
        for (PlayerWorker player : players) {
            if (player.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Is server full.
     *
     * @return the boolean
     */
    public boolean isServerFull() {
        return this.players.size() == this.maxPlayerCount;
    }

    /**
     * Start new game by assigning roles to ever player and making a new instance of GameLoop.
     *
     * @throws NotEnoughPlayerException the not enough player exception
     */
    public void startNewGame() throws NotEnoughPlayerException {
        if (!this.isServerFull()) {
            throw new NotEnoughPlayerException("server is not full");
        }

//        this.assignRolesToPlayers();

        GameLoop loop = new GameLoop();
        loop.start();
    }

    private void assignRolesToPlayers() {

    }

    /**
     * Add player worker to the list of game players.
     *
     * @param playerWorker the player worker
     */
    public void addPlayer(PlayerWorker playerWorker) {
        if (this.players.isEmpty()) {
            this.serverAdmin = playerWorker;
        }

        if (!this.isServerFull()) {
            this.players.add(playerWorker);
        }
    }

    /**
     * Server admin wants previous game load.
     *
     * @return the boolean
     */
    public boolean serverAdminWantsPreviousGameLoad() {
        try {
            Socket socket = this.serverAdmin.getSocket();
            ObjectInputStream request = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream response = new ObjectOutputStream(socket.getOutputStream());

            response.writeObject(new ShowMessageCommand("You are server admin").toString());
            response.writeObject(new GetInputCommand("Do you want to get LoadPrevious games?"));
            return (Boolean) request.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }

    /**
     * Send other players message indicating that a new player has joined the game.
     */
    public void sendPlayerConnectedMessageToOthers() {
        PlayerWorker lastAddedPlayer = this.players.get(this.players.size() - 1);
        for (PlayerWorker playerWorker : this.players) {
            if (playerWorker.equals(lastAddedPlayer)) continue;
            try {
                String message = lastAddedPlayer.getUsername() + " joined";
                playerWorker.getResponse().writeObject(new ShowMessageCommand(message).toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}

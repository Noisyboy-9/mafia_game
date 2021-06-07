package mafia.server.starter;

import mafia.server.GameRoll.citizen.*;
import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.GameRoll.mafia.DoctorLector;
import mafia.server.GameRoll.mafia.GodFather;
import mafia.server.GameRoll.mafia.NormalMafia;
import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.exceptions.InvalidMaxPlayerCountException;
import mafia.server.exceptions.NotEnoughPlayerException;
import mafia.server.exceptions.PlayerAlreadyExistException;
import mafia.server.loop.GameLoop;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The starter of the game, which handles players being added to the server and starting the game loop.
 * it also will set the GameState before the game start.
 */
public class GameStarter {
    private final ArrayList<PlayerWorker> players = new ArrayList<>();
    private final int maxPlayerCount = 10;
    private PlayerWorker serverAdmin = null;

    /**
     * Instantiates a new Game starter.
     *
     * @throws InvalidMaxPlayerCountException the invalid max player count exception
     */
    public GameStarter() throws InvalidMaxPlayerCountException {
        if (this.maxPlayerCount < 10) {
            throw new InvalidMaxPlayerCountException("Player count can not be lower then 10");
        }
    }

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

        this.assignRolesToPlayers();
        this.assignIdToPlayers();
        this.sendRoleReportToEveryPlayer();

        GameLoop loop = new GameLoop();
        loop.start();
    }

    private void assignIdToPlayers() {
        int id = 1;
        for (PlayerWorker playerWorker : this.players) {
            playerWorker.getGameRoll().setId(id);
            id++;
        }
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

    private void sendRoleReportToEveryPlayer() {
        for (PlayerWorker playerWorker : this.players) {
            ObjectOutputStream response = playerWorker.getResponse();

            try {
                response.writeObject(new ShowMessageCommand("starting game").toString());
                response.writeObject(new ShowMessageCommand(
                        "your role in game is: " + playerWorker.getGameRoll().getRollString()
                ).toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

    private void assignRolesToPlayers() {
//        shuffle the players list at the beginning to make it more random
        Collections.shuffle(this.players);

//        calculate number of mafias
        int numberOfMafias = this.maxPlayerCount / 3;

//        determine mafias
        List<PlayerWorker> mafias = new ArrayList<>(this.players.subList(0, numberOfMafias));

//        determine the citizens by removing every mafia from all players list
        List<PlayerWorker> citizens = new ArrayList<>(this.players.subList(numberOfMafias, this.maxPlayerCount));

//        create rolls list
        ArrayList<Mafia> mafiaRollsList = this.generateAvailableMafiaRollsList(numberOfMafias);
        ArrayList<Citizen> citizenRollsList = this.generateAvailableCitizenRollsList(this.maxPlayerCount - numberOfMafias);

//        assign roles to each client
        this.assignMafiaRolls(mafias, mafiaRollsList);
        this.assignCitizenRolls(citizens, citizenRollsList);

        try {
//            update game state to have list of all players with their rolls
            this.setUpGameState();
        } catch (PlayerAlreadyExistException e) {
            e.printStackTrace();
        }
    }

    private void assignCitizenRolls(List<PlayerWorker> citizens, ArrayList<Citizen> rolls) {
        while (!citizens.isEmpty()) {
            PlayerWorker citizen = this.getAndRemoveRandomElement(citizens);
            Citizen roll = this.getAndRemoveRandomElement(rolls);
            citizen.setGameRoll(roll);
        }
    }

    private void assignMafiaRolls(List<PlayerWorker> mafias, ArrayList<Mafia> rolls) {
        while (!mafias.isEmpty()) {
            PlayerWorker mafia = this.getAndRemoveRandomElement(mafias);
            Mafia roll = this.getAndRemoveRandomElement(rolls);
            mafia.setGameRoll(roll);
        }
    }

    private <Type> Type getAndRemoveRandomElement(List<Type> list) {
        if (list.size() == 1) {
            Type selectItem = list.get(0);
            list.remove(0);
            return selectItem;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(list.size() - 1);

        Type selectedItem = list.get(randomIndex);
        list.remove(randomIndex);

        return selectedItem;
    }

    private void setUpGameState() throws PlayerAlreadyExistException {
        GameState gameState = GameState.getSingletonInstance();

        for (PlayerWorker playerWorker : this.players) {
            gameState.addPlayer(playerWorker);
        }
    }

    private ArrayList<Citizen> generateAvailableCitizenRollsList(int citizensCount) {
        ArrayList<Citizen> rolls = new ArrayList<>();
        rolls.add(new CityDoctor());
        rolls.add(new Mayor());
        rolls.add(new Inspector());
        rolls.add(new Sniper());
        rolls.add(new Diehard());
        rolls.add(new Psychiatrist());

        int normalCitizensCount = citizensCount - 6;

        for (int counter = 0; counter < normalCitizensCount; counter++) {
            rolls.add(new NormalCitizen());
        }

        return rolls;
    }

    private ArrayList<Mafia> generateAvailableMafiaRollsList(int numberOfMafias) {
        ArrayList<Mafia> rolls = new ArrayList<>();
        rolls.add(new GodFather());
        rolls.add(new DoctorLector());

        int numberOfNormalMafias = numberOfMafias - 2;

        for (int counter = 0; counter < numberOfNormalMafias; counter++) {
            rolls.add(new NormalMafia());
        }

        return rolls;
    }
}

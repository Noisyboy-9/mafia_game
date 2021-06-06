package mafia.server.loop;

import mafia.server.finisher.GameFinisher;
import mafia.server.manager.GameManager;
import mafia.server.state.GameState;

import java.io.File;
import java.io.IOException;

/**
 * The Game loop handler.
 */
public class GameLoop {
    private GameManager manager;

    /**
     * Start game loop.
     */
    public void start() {
        this.manager = new GameManager();
        File database = this.createChatDatabaseFile();
        this.startIntroductionNight();

        while (!GameFinisher.isGameFinished()) {
            this.startDay();
            this.startPoll();

            if (GameFinisher.isGameFinished()) {
                break;
            }

            this.startNight();
        }

        GameFinisher.finishGame(database);
    }

    private void startIntroductionNight() {
        GameState.getSingletonInstance().goInIntroductionNightMode();
        this.manager.handleIntroductionNight();
    }

    private void startDay() {
        GameState.getSingletonInstance().goInDayMode();
        this.manager.handleDay();
    }

    private void startPoll() {
        GameState.getSingletonInstance().goInPollMode();
        this.manager.handlePoll();
    }

    private void startNight() {
        GameState.getSingletonInstance().goInNightMode();
        this.manager.handleNight();
    }

    private File createChatDatabaseFile() {
        File database = new File("src/server/mafia/server/database/chat.database.binary");

        if (!database.exists()) {
            try {
                database.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        return database;
    }
}

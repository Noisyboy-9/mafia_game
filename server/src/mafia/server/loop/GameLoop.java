package mafia.server.loop;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.enums.GameTimeEnum;
import mafia.server.finisher.GameFinisher;
import mafia.server.manager.GameManager;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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
            this.startDay(database);
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
        this.broadcastGameTimeChange(GameTimeEnum.INTRODUCTION_NIGHT);
        this.manager.handleIntroductionNight();
    }

    private void startDay(File database) {
        GameState.getSingletonInstance().goInDayMode();
        this.broadcastGameTimeChange(GameTimeEnum.DAY);
        this.manager.handleDay(database);
    }

    private void startPoll() {
        GameState.getSingletonInstance().goInPollMode();
        this.broadcastGameTimeChange(GameTimeEnum.POLL);
        this.manager.handlePoll();
    }

    private void startNight() {
        GameState.getSingletonInstance().goInNightMode();
        this.broadcastGameTimeChange(GameTimeEnum.NIGHT);
        this.manager.handleNight();
    }

    private File createChatDatabaseFile() {
        return new File("src/mafia/server/database/chat.database.binary");
    }

    private void broadcastGameTimeChange(GameTimeEnum newTime) {
        ArrayList<PlayerWorker> allPlayers = GameState.getSingletonInstance().getAllGamePlayers();
        for (PlayerWorker playerWorker : allPlayers) {
            ObjectOutputStream response = playerWorker.getResponse();
            String message;

            if (newTime.equals(GameTimeEnum.DAY)) {
                message = "Starting Day\nWhen ever you want to chat just type and hit enter\nIf you are ready type 'ready'";
            } else if (newTime.equals(GameTimeEnum.NIGHT)) {
                message = "Starting Night";
            } else if (newTime.equals(GameTimeEnum.INTRODUCTION_NIGHT)) {
                message = "Starting Introduction Night";
            } else {
                message = "Starting Poll";
            }

            try {
                response.writeObject(new ShowMessageCommand(message).toString());
            } catch (IOException ignored) {
            }
        }
    }
}

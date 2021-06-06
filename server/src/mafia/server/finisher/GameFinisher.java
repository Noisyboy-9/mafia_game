package mafia.server.finisher;

import mafia.server.commands.ShowMessageCommand;
import mafia.server.enums.GameWinnerEnum;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The type Game finisher.
 */
public class GameFinisher {
    private static GameWinnerEnum gameWinner;

    /**
     * check if the game is finished.
     *
     * @return the boolean
     */
    public static boolean isGameFinished() {
        return mafiaIsWinner() || citizenIsWinner();
    }

    /**
     * do game finishing process.
     *
     * @param database the database
     */
    public static void finishGame(File database) {
        closeAllSockets();
        clearChatDatabase(database);
    }

    private static boolean citizenIsWinner() {
        if (GameState.mafiaCount() == 0) {
            gameWinner = GameWinnerEnum.CITIZEN;
            return true;
        }

        return false;
    }

    private static void closeAllSockets() {
        for (PlayerWorker playerWorker : GameState.getSingletonInstance().getAllGamePlayers()) {
            try {
                sendGameFinishCommand(playerWorker);
                playerWorker.getSocket().close();
            } catch (IOException ignored) {
            }
        }
    }

    private static void sendGameFinishCommand(PlayerWorker playerWorker) throws IOException {
        ObjectOutputStream response = playerWorker.getResponse();
        String message = gameWinner == GameWinnerEnum.MAFIA ? "mafias win the game" : "citizens win the game";
        response.writeObject(new ShowMessageCommand(message));
    }

    private static boolean mafiaIsWinner() {
        if (GameState.mafiaCount() >= GameState.citizenCount()) {
            gameWinner = GameWinnerEnum.MAFIA;
            return true;
        }

        return false;
    }

    private static void clearChatDatabase(File database) {
        database.delete();
    }
}

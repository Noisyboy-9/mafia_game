package mafia.server.state.managers;

import mafia.server.enums.GameTimeEnum;

/**
 * The type Game time manager.
 */
public class GameTimeManager {
    /**
     * The Game time enum.
     */
    public GameTimeEnum gameTimeEnum;

    /**
     * Instantiates a new Game time manager.
     */
    public GameTimeManager() {
    }

    /**
     * Go in night mode.
     */
    public void goInNightMode() {
        this.gameTimeEnum = GameTimeEnum.NIGHT;
    }

    /**
     * Go in day mode.
     */
    public void goInDayMode() {
        this.gameTimeEnum = GameTimeEnum.DAY;
    }

    /**
     * Go in poll mode.
     */
    public void goInPollMode() {
        this.gameTimeEnum = GameTimeEnum.POLL;
    }

    /**
     * Go in introduction night.
     */
    public void goInIntroductionNightMode() {
        this.gameTimeEnum = GameTimeEnum.INTRODUCTION_NIGHT;
    }
}
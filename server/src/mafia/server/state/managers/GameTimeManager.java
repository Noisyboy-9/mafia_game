package mafia.server.state.managers;

import mafia.server.enums.GameTimeEnum;

public class GameTimeManager {
    public GameTimeEnum gameTimeEnum;

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
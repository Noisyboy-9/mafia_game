package mafia.server.manager;

import mafia.server.manager.traits.CanHandleDayTrait;
import mafia.server.manager.traits.CanHandleIntroductionNightTrait;
import mafia.server.manager.traits.CanHandleNightTrait;
import mafia.server.manager.traits.CanHandlePollTrait;

/**
 * The type Game manager.
 */
public class GameManager implements CanHandleNightTrait,
        CanHandleIntroductionNightTrait,
        CanHandlePollTrait,
        CanHandleDayTrait {

}
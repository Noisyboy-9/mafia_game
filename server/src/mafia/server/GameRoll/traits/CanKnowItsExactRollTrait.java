package mafia.server.GameRoll.traits;

import mafia.server.GameRoll.citizen.*;
import mafia.server.GameRoll.citizen.abstracts.Citizen;
import mafia.server.GameRoll.mafia.DoctorLector;
import mafia.server.GameRoll.mafia.GodFather;
import mafia.server.GameRoll.mafia.NormalMafia;
import mafia.server.GameRoll.mafia.abstacts.Mafia;

/**
 * The interface Can know its exact roll trait.
 */
public interface CanKnowItsExactRollTrait {
    /**
     * Check if player is mafia.
     *
     * @return the boolean
     */
    default boolean isMafia() {
        return this instanceof Mafia;
    }

    /**
     * Is citizen boolean.
     *
     * @return the boolean
     */
    default boolean isCitizen() {
        return this instanceof Citizen;
    }

    /**
     * Is city doctor boolean.
     *
     * @return the boolean
     */
    default boolean isCityDoctor() {
        return this instanceof CityDoctor;
    }

    /**
     * Is inspector boolean.
     *
     * @return the boolean
     */
    default boolean isInspector() {
        return this instanceof Inspector;
    }

    /**
     * Is mayor boolean.
     *
     * @return the boolean
     */
    default boolean isMayor() {
        return this instanceof Mayor;
    }

    /**
     * Is sniper boolean.
     *
     * @return the boolean
     */
    default boolean isSniper() {
        return this instanceof Sniper;
    }

    /**
     * Is die hard boolean.
     *
     * @return the boolean
     */
    default boolean isDieHard() {
        return this instanceof Diehard;
    }

    /**
     * Is normal citizen boolean.
     *
     * @return the boolean
     */
    default boolean isNormalCitizen() {
        return this instanceof NormalCitizen;
    }

    /**
     * Is psychiatrist boolean.
     *
     * @return the boolean
     */
    default boolean isPsychiatrist() {
        return this instanceof Psychiatrist;
    }

    /**
     * Is god father boolean.
     *
     * @return the boolean
     */
    default boolean isGodFather() {
        return this instanceof GodFather;
    }

    /**
     * Is normal mafia boolean.
     *
     * @return the boolean
     */
    default boolean isNormalMafia() {
        return this instanceof NormalMafia;
    }

    /**
     * Is doctor lector boolean.
     *
     * @return the boolean
     */
    default boolean isDoctorLector() {
        return this instanceof DoctorLector;
    }

    /**
     * Gets roll string.
     *
     * @return the roll string
     */
    default String getRollString() {
        if (this.isCitizen()) {
            if (this.isCityDoctor()) return "city doctor";
            if (this.isDieHard()) return "die hard";
            if (this.isInspector()) return "inspector";
            if (this.isMayor()) return "mayor";
            if (this.isPsychiatrist()) return "psychiatrist";
            if (this.isSniper()) return "sniper";
            else return "normal citizen";
        } else {
            if (this.isGodFather()) return "god father";
            if (this.isDoctorLector()) return "doctor lector";
            else return "normal mafia";
        }
    }
}


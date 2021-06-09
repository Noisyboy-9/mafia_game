package mafia.server.manager.traits;

import mafia.server.GameRoll.citizen.*;
import mafia.server.GameRoll.mafia.DoctorLector;
import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.exceptions.BottomMafiaCanNotKillCitizenException;
import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.runnables.voting.MafiaVoteGetterRunnable;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The interface Can handle night trait.
 */
public interface CanHandleNightTrait extends CanHandlePlayerDisconnect {
    /**
     * Handle night.
     */
    default void handleNight() {
        PlayerWorker mafiaKillTarget = null;

        try {
            mafiaKillTarget = this.handleMafiaCitizenKill();
        } catch (PlayerIsAlreadyDeadException e) {
            PlayerWorker mafiaLeader = GameState.getSingletonInstance().getMafiaLeader();
            ObjectOutputStream response = Objects.requireNonNull(mafiaLeader).getResponse();
            try {
                response.writeObject(new ShowMessageCommand("Player is already Dead!").toString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(mafiaLeader);
            }
        }
        this.handleMafiaDoctor();

        this.handleCityDoctor();
        this.handleCityInspector();
        PlayerWorker sniperShootResult = this.handleCitySniper();
        this.handleCityPsychiatrist();
        this.handleDieHard();

        if (!Objects.requireNonNull(mafiaKillTarget).isAlive()) {
            try {
                GameState.getSingletonInstance().killPlayer(mafiaKillTarget);
            } catch (PlayerIsAlreadyDeadException e) {
                e.printStackTrace();
            }

            this.broadcastMessageToAll(mafiaKillTarget.getUsername() + " is killed!");
        }

        if (!Objects.isNull(sniperShootResult)) {
            try {
                GameState.getSingletonInstance().killPlayer(mafiaKillTarget);
            } catch (PlayerIsAlreadyDeadException e) {
                e.printStackTrace();
            }

            this.broadcastMessageToAll(sniperShootResult.getUsername() + " is killed!");
        }
    }

    private void handleDieHard() {
        PlayerWorker dieHardWorker = GameState.getSingletonInstance().getDiehard();
        if (Objects.isNull(dieHardWorker)) {
//            die hard is dead
            return;
        }

        Diehard diehard = (Diehard) dieHardWorker.getGameRoll();
        if (diehard.wantsGameReport(dieHardWorker)) {
            try {
                diehard.sendGameReportString(dieHardWorker, diehard.getGameReportString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(dieHardWorker);
            }
        }
    }

    private void handleCityPsychiatrist() {
        PlayerWorker psychiatristWorker = GameState.getSingletonInstance().getPsychiatrist();

        if (Objects.isNull(psychiatristWorker)) {
//            psychiatrist is null
            return;
        }

        Psychiatrist psychiatrist = (Psychiatrist) psychiatristWorker.getGameRoll();
        psychiatrist.selectPlayerToMute(psychiatristWorker);
    }

    private PlayerWorker handleCitySniper() {
        PlayerWorker sniperWorker = GameState.getSingletonInstance().getSniper();

        if (Objects.isNull(sniperWorker)) {
//            sniper is dead
            return null;
        }

        Sniper sniper = (Sniper) sniperWorker.getGameRoll();
        if (sniper.wantToAct(sniperWorker)) {
            return sniper.shootPlayer(sniperWorker);
        }

        return null;
    }

    private void handleCityInspector() {
        PlayerWorker inspectorWorker = GameState.getSingletonInstance().getInspector();

        if (Objects.isNull(inspectorWorker)) {
//            city inspector is dead
            return;
        }

        Inspector inspector = (Inspector) inspectorWorker.getGameRoll();
        inspector.getPlayerReport(inspectorWorker);
    }

    private void handleCityDoctor() {
        PlayerWorker cityDoctorWorker = GameState.getSingletonInstance().getCityDoctor();

        if (Objects.isNull(cityDoctorWorker)) {
//            city doctor is dead
            return;
        }

        CityDoctor cityDoctor = (CityDoctor) cityDoctorWorker.getGameRoll();
        cityDoctor.selectPlayerToCure(cityDoctorWorker).getGameRoll().revive();
    }

    private void handleMafiaDoctor() {
        PlayerWorker doctorLectorWorker = GameState.getSingletonInstance().getDoctorLector();

        if (Objects.isNull(doctorLectorWorker)) {
//            doctor lector is dead
            return;
        }

        DoctorLector doctorLector = (DoctorLector) doctorLectorWorker.getGameRoll();
        doctorLector.selectMafiaToCure(doctorLectorWorker).getGameRoll().revive();
    }

    private PlayerWorker handleMafiaCitizenKill() throws PlayerIsAlreadyDeadException {
        HashMap<PlayerWorker, PlayerWorker> votes = this.getBottomMafiasKillTargetVote();
        PlayerWorker mafiaKillTarget = this.getMafiaLeaderKillTarget(votes);
        mafiaKillTarget.getGameRoll().kill();
        return mafiaKillTarget;
    }

    private PlayerWorker getMafiaLeaderKillTarget(HashMap<PlayerWorker, PlayerWorker> votes) {
        PlayerWorker mafiaLeaderWorker = GameState.getSingletonInstance().getMafiaLeader();
        Mafia mafiaLeader = (Mafia) Objects.requireNonNull(mafiaLeaderWorker).getGameRoll();

        try {
            mafiaLeader.showOtherMafiasVote(votes, mafiaLeaderWorker);
        } catch (IOException ioException) {
            this.handlePlayerDisconnect(mafiaLeaderWorker);
        }

        PlayerWorker killTarget = null;

        try {
            killTarget = mafiaLeader.selectCitizenToKill(mafiaLeaderWorker);
        } catch (IOException ioException) {
            this.handlePlayerDisconnect(mafiaLeaderWorker);
        } catch (BottomMafiaCanNotKillCitizenException e) {
            e.printStackTrace();
        }

        return killTarget;
    }

    private HashMap<PlayerWorker, PlayerWorker> getBottomMafiasKillTargetVote() {
        ArrayList<PlayerWorker> mafias = GameState.getSingletonInstance().getAliveMafias();
        HashMap<PlayerWorker, PlayerWorker> votes = new HashMap<>();


        ExecutorService executor = null;

        for (PlayerWorker mafiaWorker : mafias) {
            Mafia mafia = (Mafia) mafiaWorker.getGameRoll();
            executor = Executors.newCachedThreadPool();
            if (!mafia.isLeader()) {
                executor.execute(new MafiaVoteGetterRunnable(mafiaWorker, votes));
            }
        }

        try {
            Objects.requireNonNull(executor).shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Objects.requireNonNull(executor).awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return votes;
    }
}

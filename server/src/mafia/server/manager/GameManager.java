package mafia.server.manager;

import mafia.server.GameRoll.citizen.*;
import mafia.server.GameRoll.mafia.DoctorLector;
import mafia.server.GameRoll.mafia.abstacts.Mafia;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.exceptions.BottomMafiaCanNotKillCitizenException;
import mafia.server.exceptions.PlayerIsAlreadyDeadException;
import mafia.server.state.GameState;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GameManager {
    public void handleIntroductionNight() {
        this.introduceMafias();
        this.introduceCityDoctorToMayor();
    }

    public void handleNight() {
        try {
            this.handleMafiaCitizenKill();
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
        this.handleCitySniper();
        this.handleCityPsychiatrist();
        this.handleDieHard();
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

    private void handleCitySniper() {
        PlayerWorker sniperWorker = GameState.getSingletonInstance().getSniper();

        if (Objects.isNull(sniperWorker)) {
//            sniper is dead
            return;
        }

        Sniper sniper = (Sniper) sniperWorker.getGameRoll();
        if (sniper.wantToAct(sniperWorker)) {
            sniper.shootPlayer(sniperWorker);
        }
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

    private void handleMafiaCitizenKill() throws PlayerIsAlreadyDeadException {
        HashMap<PlayerWorker, PlayerWorker> votes = this.getBottomMafiasKillTargetVote();
        PlayerWorker mafiaKillTarget = this.getMafiaLeaderKillTarget(votes);
        GameState.getSingletonInstance().killPlayer(mafiaKillTarget);
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
        ArrayList<PlayerWorker> mafias = GameState.getSingletonInstance().getAllGamePlayers();
        HashMap<PlayerWorker, PlayerWorker> votes = new HashMap<>();
        for (PlayerWorker mafiaWorker : mafias) {
            Mafia mafia = (Mafia) mafiaWorker.getGameRoll();
            if (!mafia.isLeader()) {
                votes.put(mafiaWorker, mafia.voteForCitizenToKill(mafiaWorker));
            }
        }
        return votes;
    }

    private void introduceCityDoctorToMayor() {
        PlayerWorker mayor = GameState.getSingletonInstance().getMayor();
        PlayerWorker cityDoctor = GameState.getSingletonInstance().getCityDoctor();

        if (mayor != null && cityDoctor != null) {
            ObjectOutputStream mayorResponse = mayor.getResponse();
            try {
                mayorResponse.writeObject(new ShowMessageCommand(cityDoctor.getUsername() + " is city doctor").toString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(mayor);
            }
        }
    }

    private void introduceMafias() {
        ArrayList<PlayerWorker> mafias = GameState.getSingletonInstance().getAllGamePlayers();
        String mafiaString = this.generateMafiaIntroductionString();

        for (PlayerWorker mafia : mafias) {
            ObjectOutputStream response = mafia.getResponse();
            try {
                response.writeObject(new ShowMessageCommand(mafiaString).toString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(mafia);
            }
        }
    }

    private void handlePlayerDisconnect(PlayerWorker playerWorker) {
        this.broadcastMessageToAll(playerWorker.getUsername());
        try {
            GameState.getSingletonInstance().killPlayer(playerWorker);
        } catch (PlayerIsAlreadyDeadException ignored) {
        }
    }

    private void broadcastMessageToAll(String message) {
        ArrayList<PlayerWorker> playerWorkers = GameState.getSingletonInstance().getAllGamePlayers();
        for (PlayerWorker playerWorker : playerWorkers) {
            ObjectOutputStream response = playerWorker.getResponse();

            try {
                response.writeObject(new ShowMessageCommand(message).toString());
            } catch (IOException ignored) {
            }
        }
    }

    private String generateMafiaIntroductionString() {
        StringBuilder builder = new StringBuilder();

        PlayerWorker godFather = GameState.getSingletonInstance().getGodFather();
        PlayerWorker doctorLector = GameState.getSingletonInstance().getDoctorLector();
        ArrayList<PlayerWorker> normalMafias = GameState.getSingletonInstance().getNormalMafias();


        if (godFather != null) {
            builder.append(godFather.getUsername()).append(" is god father.").append("\n");
        }

        if (doctorLector != null) {
            builder.append(doctorLector.getUsername()).append(" is doctor lector.").append("\n");
        }

        if (normalMafias != null) {
            for (PlayerWorker normalMafia : normalMafias) {
                builder.append(normalMafia.getUsername()).append(" is normal mafia.").append("\n");
            }
        }

        return builder.toString();
    }
}
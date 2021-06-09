package mafia.server.ChatSystem;

import mafia.server.workers.PlayerWorker;

import java.io.Serializable;

public class Message implements Serializable {
    private final String body;
    private final PlayerWorker sender;

    public Message(PlayerWorker sender, String body) {
        this.body = body;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return sender.getUsername() + " : " + body;
    }

    public String getBody() {
        return body;
    }

    public PlayerWorker getSender() {
        return sender;
    }
}

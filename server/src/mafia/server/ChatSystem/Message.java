package mafia.server.ChatSystem;

import java.io.Serializable;

public class Message implements Serializable {
    private final String body;
    private final String senderUsername;

    public Message(String senderUsername, String body) {
        this.body = body;
        this.senderUsername = senderUsername;
    }

    @Override
    public String toString() {
        return this.senderUsername + " : " + body;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getBody() {
        return body;
    }

}

package mafia.server.ChatSystem;

import java.io.Serializable;

/**
 * The type Message.
 */
public class Message implements Serializable {
    private final String body;
    private final String senderUsername;

    /**
     * Instantiates a new Message.
     *
     * @param senderUsername the sender username
     * @param body           the body
     */
    public Message(String senderUsername, String body) {
        this.body = body;
        this.senderUsername = senderUsername;
    }

    @Override
    public String toString() {
        return this.senderUsername + " : " + body;
    }

    /**
     * Gets sender username.
     *
     * @return the sender username
     */
    public String getSenderUsername() {
        return senderUsername;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

}

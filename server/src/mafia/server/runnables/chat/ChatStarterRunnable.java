package mafia.server.runnables.chat;

import mafia.server.ChatSystem.ChatServer;
import mafia.server.ChatSystem.Message;
import mafia.server.manager.traits.CanHandlePlayerDisconnect;
import mafia.server.workers.PlayerWorker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class ChatStarterRunnable implements Runnable, CanHandlePlayerDisconnect {
    private final PlayerWorker user;
    private final ChatServer server;
    private final List<Message> messages;

    public ChatStarterRunnable(PlayerWorker user, List<Message> messages, ChatServer server) {
        this.user = user;
        this.messages = messages;
        this.server = server;
    }

    @Override
    public void run() {
        ObjectOutputStream response = this.user.getResponse();
        ObjectInputStream request = this.user.getRequest();

        try {
            while (true) {
                String messageBody = (String) request.readObject();
                Message message = new Message(this.user, messageBody);

                if (message.getBody().equalsIgnoreCase("history")) {
                    this.server.sendMessagesToPerson(this.messages, this.user);
                    continue;
                }

                if (message.getBody().equalsIgnoreCase("ready")) {
                    this.server.closeUserChat(user);
                    break;
                }

                synchronized (this.messages) {
                    this.messages.add(message);
                }

                this.server.broadcast(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            this.handlePlayerDisconnect(this.user);
        }
    }
}

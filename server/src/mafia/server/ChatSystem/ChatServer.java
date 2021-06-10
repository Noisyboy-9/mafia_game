package mafia.server.ChatSystem;

import mafia.server.commands.CloseChatCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.commands.StartChatCommand;
import mafia.server.manager.traits.CanHandlePlayerDisconnect;
import mafia.server.runnables.chat.ChatStarterRunnable;
import mafia.server.workers.PlayerWorker;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The type Chat server.
 */
public class ChatServer implements CanHandlePlayerDisconnect {
    private final File database;
    private final ArrayList<PlayerWorker> users;
    private final List<Message> messages;

    /**
     * Instantiates a new Chat server.
     *
     * @param database the database
     */
    public ChatServer(File database) {
        this.database = database;
        this.users = new ArrayList<>();
        this.messages = this.readPreviousMessages();
    }

    /**
     * Add user.
     *
     * @param user the user
     */
    public void addUser(PlayerWorker user) {
        this.users.add(user);
    }

    /**
     * Start.
     *
     * @param limit the limit
     * @param unit  the unit
     */
    public void start(int limit, TimeUnit unit) {
        this.broadcastStartChatCommand();
        this.startChat(limit, unit);
        this.broadcastCloseChatCommand();
        this.writeMessagesToDatabase();
    }

    /**
     * Broadcast a single message.
     *
     * @param message the message
     */
    public void broadcast(Message message) {
        for (PlayerWorker user : this.users) {
            if (!message.getSenderUsername().equals(user.getUsername())) {
//                don't sent the sender's message again to himself
                ObjectOutputStream response = user.getResponse();
                try {
                    response.writeObject(new ShowMessageCommand(message.toString()).toString());
                } catch (IOException ioException) {
                    this.handlePlayerDisconnect(user);
                }
            }
        }
    }

    /**
     * Send messages to person.
     *
     * @param messages the messages
     * @param user     the user
     */
    public void sendMessagesToPerson(List<Message> messages, PlayerWorker user) {
        ObjectOutputStream response = user.getResponse();
        try {
            response.writeObject(new ShowMessageCommand("all messages:").toString());
        } catch (IOException ioException) {
            this.handlePlayerDisconnect(user);
        }

        for (Message message : messages) {
            try {
                response.writeObject(new ShowMessageCommand(message.toString()).toString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(user);
            }
        }
    }

    /**
     * Close user chat.
     *
     * @param user the user
     */
    public void closeUserChat(PlayerWorker user) {
        ObjectOutputStream response = user.getResponse();
        try {
            response.writeObject(new CloseChatCommand().toString());
        } catch (IOException ioException) {
            this.handlePlayerDisconnect(user);
        }
    }

    private List<Message> readPreviousMessages() {
        if (database.length() == 0) {
            return Collections.synchronizedList(new ArrayList<>());
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(database))) {
            return Collections.synchronizedList((List<Message>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Collections.synchronizedList(new ArrayList<>());
    }

    private void writeMessagesToDatabase() {
        ArrayList<Message> allMessages = new ArrayList<>(this.messages);

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.database));
            objectOutputStream.writeObject(allMessages);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void broadcastCloseChatCommand() {
        for (PlayerWorker user : this.users) {
            ObjectOutputStream response = user.getResponse();
            try {
                response.writeObject(new CloseChatCommand().toString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(user);
            }
        }
    }

    private void broadcastStartChatCommand() {
        for (PlayerWorker user : this.users) {
            ObjectOutputStream response = user.getResponse();
            try {
                response.writeObject(new StartChatCommand().toString());
            } catch (IOException ioException) {
                this.handlePlayerDisconnect(user);
            }
        }
    }

    private void startChat(int limit, TimeUnit unit) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (PlayerWorker user : this.users) {
            executorService.execute(new ChatStarterRunnable(user, this.messages, this));
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(limit, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

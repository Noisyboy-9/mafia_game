package mafia.server.ChatSystem;

import mafia.server.commands.CloseChatCommand;
import mafia.server.commands.ShowMessageCommand;
import mafia.server.commands.StartChatCommand;
import mafia.server.manager.traits.CanHandlePlayerDisconnect;
import mafia.server.runnables.chat.ChatStarterRunnable;
import mafia.server.workers.PlayerWorker;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The type Chat server.
 */
public class ChatServer implements CanHandlePlayerDisconnect {
    private final File database;
    private final ArrayList<PlayerWorker> users;
    private final ArrayList<Message> messages;

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
            if (!message.getSender().equals(user)) {
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
     * Broadcast A list of messages.
     *
     * @param messages the messages
     */
    public void broadcast(ArrayList<Message> messages) {
        messages.forEach(this::broadcast);
    }

    private ArrayList<Message> readPreviousMessages() {
        ArrayList<Message> messages = new ArrayList<>();

        try {
            InputStream in = new FileInputStream(database);
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            messages.add((Message) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return messages;
    }

    private void writeMessagesToDatabase() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(this.database));
            outputStream.writeObject(this.messages);
            outputStream.flush();
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
            executorService.execute(new ChatStarterRunnable(user, messages, this));
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(limit, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

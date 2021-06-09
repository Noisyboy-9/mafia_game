package mafia.server.commands;

public class ShowChatMessageCommand implements Command {
    private final String messageString;

    public ShowChatMessageCommand(String messageString) {
        this.messageString = messageString;
    }

    @Override
    public String toString() {
        return messageString;
    }
}

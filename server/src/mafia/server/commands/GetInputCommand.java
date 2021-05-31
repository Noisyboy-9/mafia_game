package mafia.server.commands;

public class GetInputCommand implements Command {
    private final String question;

    public GetInputCommand(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "GetInput" + " " + this.question;
    }
}


package mafia.server.commands;

/**
 * Get input command.
 */
public class GetInputCommand implements Command {
    private final String question;

    @Override
    public String toString() {
        return "GetInput" + " " + this.question;
    }

    /**
     * Instantiates a new Get input command.
     *
     * @param question the question
     */
    public GetInputCommand(String question) {
        this.question = question;
    }
}


package mafia.client.command;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The type Get input command.
 */
public class GetInputCommand extends Command {
    private final ArrayList<String> questionWords = new ArrayList<>();
    private final ObjectOutputStream request;

    /**
     * Instantiates a new Get input command.
     *
     * @param commandString the command string
     * @param request       the request
     */
    public GetInputCommand(String commandString, ObjectOutputStream request) {
        super(commandString);
        this.request = request;
        this.questionWords.addAll(Arrays.asList(tokens).subList(1, tokens.length));
    }


    @Override
    public void handle() {
        String userInput = this.getUserInput();
        try {
            this.request.writeObject(userInput);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private String getUserInput() {
        System.out.println(String.join(" ", this.questionWords));
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your response:\t");
        return scanner.nextLine();
    }
}

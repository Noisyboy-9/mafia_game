package mafia.client.command;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GetInputCommand extends Command {
    private final ArrayList<String> questionWords = new ArrayList<>();
    private final ObjectOutputStream request;

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
        System.out.print("please input text: ");
        return scanner.nextLine();
    }
}

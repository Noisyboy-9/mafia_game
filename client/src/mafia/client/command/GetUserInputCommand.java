package mafia.client.command;

import java.util.Scanner;

public class GetUserInputCommand extends Command {
    private final String question;

    public GetUserInputCommand(String commandString) {
        super(commandString);
        this.question = commandString.split("//s+")[1];
    }


    @Override
    public void handle() {
        String userInput = this.getUserInput();

//        TODO: send user input to sever
    }

    private String getUserInput() {
        System.out.println(this.question);
        Scanner scanner = new Scanner(System.in);
        System.out.print("please input text: ");
        return scanner.nextLine();
    }
}

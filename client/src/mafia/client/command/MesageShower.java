package mafia.client.command;

public class MesageShower extends Command {
    private final String message;

    public MesageShower(String commandString) {
        super(commandString);
        this.message = commandString.split("//s+")[1];
    }

    @Override
    public void handle() {
        System.out.println(message);
    }
}

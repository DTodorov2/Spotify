package bg.sofia.uni.fmi.mjt.spotify.command.commands;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public abstract class Command {

    protected String[] arguments;
    protected String userInput;
    protected int argsCount;

    public Command(String[] arguments, int argsCount) {
        validateArrayOfString(arguments);
        validateInt(argsCount, "count of the expected arguments");
        this.argsCount = argsCount;
        this.arguments = arguments;
    }

    public Command(String userInput) {
        validateString(userInput, "user input");
        this.userInput = userInput;

        arguments = null;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        validateArrayOfString(arguments);

        this.arguments = arguments;
    }

    public abstract String execute() throws ChannelCommunicationException, InvalidArgumentsCountException;

}

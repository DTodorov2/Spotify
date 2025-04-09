package bg.sofia.uni.fmi.mjt.spotify.command.commands;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;

public abstract class Command {

    private static final String ARGUMENT_SEPARATOR = " ";

    protected final String[] arguments;
    protected String userInput = null;

    public Command(String[] arguments, int expectedCountArguments) {
        validateArrayOfString(arguments);
        validateInt(expectedCountArguments, "count of the expected arguments");
        validateArgCount(arguments, expectedCountArguments);

        this.arguments = arguments;
    }

    public String[] getArguments() {
        return arguments;
    }

    public abstract String execute() throws ChannelCommunicationException;
}

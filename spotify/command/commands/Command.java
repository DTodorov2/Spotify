package bg.sofia.uni.fmi.mjt.spotify.command.commands;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;

public abstract class Command {

//    private static final String INVALID_ARGS_COUNT_MESSAGE_FORMAT =
//            "Invalid count of arguments: \"%s\" expects %d arguments. Example: \"%s\"";

    protected final String[] arguments;

    public Command(String[] arguments, int expectedCountArguments) {
        validateArrayOfString(arguments);
        validateInt(expectedCountArguments, "count of the arguments");
        validateArgCount(arguments, expectedCountArguments);
        //checkArgumentNotNull(usersRepository, "user repository");

        this.arguments = arguments;
        //this.usersRepository = usersRepository;
    }

    public String[] getArguments() {
        return arguments;
    }

    public abstract String execute();
}

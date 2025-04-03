package bg.sofia.uni.fmi.mjt.spotify.command.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;

public abstract class Command {

//    private static final String INVALID_ARGS_COUNT_MESSAGE_FORMAT =
//            "Invalid count of arguments: \"%s\" expects %d arguments. Example: \"%s\"";

    protected final String[] arguments;
    protected SelectionKey key;
    protected UsersRepository usersRepository;

    public Command(String[] arguments, int expectedCountArguments, SelectionKey key, UsersRepository usersRepository) {
        validateArrayOfString(arguments);
        validateInt(expectedCountArguments, "count of the arguments");
        validateArgCount(arguments, expectedCountArguments);
        checkArgumentNotNull(key, "selection key");
        checkArgumentNotNull(usersRepository, "user repository");

        this.arguments = arguments;
        this.key = key;
        this.usersRepository = usersRepository;
    }

    public String[] getArguments() {
        return arguments;
    }

    public boolean isUserLogged() {
        //trqq li mi proverka dali key e null
        String userId = (String) key.attachment();
        return userId != null;

    }

    public abstract String execute();
}

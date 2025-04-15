package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidUserAuthenticationException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.ERROR_USER_CREDENTIALS;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.FAIL_REGISTER;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_REGISTER;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateUserCredentials;

public class RegisterCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public RegisterCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        String userEmail = arguments[0];
        String userPass = arguments[1];
        try {
            validateUserCredentials(userEmail, userPass);
        } catch (InvalidUserAuthenticationException e) {
            return ERROR_USER_CREDENTIALS + e.getMessage();
        }
        User newUser = new User(userEmail, userPass, new PlaylistsRepository());
        UsersRepository usersRepository = serverResources.getUsersRepository();

        return usersRepository.add(newUser) ?  SUCCESS_REGISTER : FAIL_REGISTER;
    }
}

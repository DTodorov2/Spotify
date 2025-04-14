package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidUserAuthenticationException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateUserCredentials;

public class RegisterCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public RegisterCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        String userEmail = arguments[0];
        String userPass = arguments[1];
        try {
            validateUserCredentials(userEmail, userPass);
        } catch (InvalidUserAuthenticationException e) {
            return "There was an error with the credentials. " + e.getMessage();
        }
        User newUser = new User(userEmail, userPass, new PlaylistsRepository());
        UsersRepository usersRepository = serverResources.getUsersRepository();

        String successMessage = "The user is registered successfully!";
        String failMessage = "This email is taken! You cannot be registered!";
        return usersRepository.add(newUser) ?  successMessage : failMessage;
    }
}

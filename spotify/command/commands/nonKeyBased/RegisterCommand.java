package bg.sofia.uni.fmi.mjt.spotify.command.commands.nonKeyBased;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidUserAuthenticationException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateUserCredentials;

public class RegisterCommand extends Command {

    private static final int EXPECTED_INPUT_LENGTH = 3;

    public RegisterCommand(String[] arguments, UsersRepository usersRepository) {
        super(arguments, EXPECTED_INPUT_LENGTH, usersRepository);
        checkArgumentNotNull(usersRepository, "user repository");

        this.usersRepository = usersRepository;
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
        User newUser = new User(userEmail, userPass);
        return usersRepository.add(newUser) ? "The user is registered successfully!" : "The user cannot be registered!";
    }
}

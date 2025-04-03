package bg.sofia.uni.fmi.mjt.spotify.command.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.security.SHA256;

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class LoginCommand extends Command {

    private static final int EXPECTED_INPUT_LENGTH = 3;

    public LoginCommand(String[] arguments, UsersRepository usersRepository, SelectionKey key) {
        super(arguments, EXPECTED_INPUT_LENGTH, key, usersRepository);
        checkArgumentNotNull(usersRepository, "user repository");

        this.usersRepository = usersRepository;
    }

    @Override
    public String execute() {
        String userEmail = arguments[0];
        String userPass = arguments[1];

        User currUser = usersRepository.getMap().get(userEmail);

        if (currUser == null) {
            return "No such user!";
        }

        if (!currUser.getPassword().equals(SHA256.hash(userPass))) {
            return "Incorrect password!";
        }

        currUser.setLoggedIn(true);
        key.attach(currUser.getId()); // za da mojem da razlichavame potrebitelite
        return "The user is successfully logged in";
    }
}

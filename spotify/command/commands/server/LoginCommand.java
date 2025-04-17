package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import bg.sofia.uni.fmi.mjt.spotify.server.security.SHA256;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.ALREADY_LOGGED_IN;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.INCORRECT_PASSWORD;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGOUT_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_LOGIN;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.USER_DOES_NOT_EXIST;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class LoginCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public LoginCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser != null) {
            return LOGOUT_REQUIRED;
        }

        String userEmail = arguments[0];
        String userPass = arguments[1];

        User currUser = serverResources.getUsersRepository().getMap().get(userEmail);

        if (currUser == null) {
            return USER_DOES_NOT_EXIST;
        }

        if (!SHA256.checkPassword(userPass, currUser.getPassword())) {
            return INCORRECT_PASSWORD;
        }

        if (currUser.isLoggedIn()) {
            return ALREADY_LOGGED_IN;
        }

        currUser.setLoggedIn(true);
        serverResources.getSelectionKey().attach(currUser.getId());
        return SUCCESS_LOGIN;
    }
}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class LogoutCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 0;

    public LogoutCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return "You are not logged in!";
        }

        loggedUser.setLoggedIn(false);
        serverResources.getSelectionKey().attach(null);
        return "You logged out successfully!";
    }
}

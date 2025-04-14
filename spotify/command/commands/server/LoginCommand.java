package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import bg.sofia.uni.fmi.mjt.spotify.server.security.SHA256;

public class LoginCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public LoginCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser != null) {
            return "You must logout first in order to login!";
        }

        String userEmail = arguments[0];
        String userPass = arguments[1];

        User currUser = serverResources.getUsersRepository().getMap().get(userEmail);

        if (currUser == null) {
            return "No such user!";
        }

        if (!currUser.getPassword().equals(SHA256.hash(userPass))) {
            return "Incorrect password!";
        }

        if (currUser.isLoggedIn()) {
            return "The current user is already logged in!";
        }

        currUser.setLoggedIn(true);
        serverResources.getSelectionKey().attach(currUser.getId()); // za da mojem da razlichavame potrebitelite
        return "The user is successfully logged in!";
    }
}

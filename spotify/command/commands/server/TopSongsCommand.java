package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class TopSongsCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 1;

    public TopSongsCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        boolean isUserLogged = serverResources.isUserLogged();
        if (!isUserLogged) {
            return "You must be logged in in order to use the top songs option";
        }

        int argAsInt = Integer.parseInt(arguments[0]);
        return serverResources.getSongsRepository().getTopStatistics(argAsInt).toString();

    }
}

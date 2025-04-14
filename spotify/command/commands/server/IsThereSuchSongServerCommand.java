package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class IsThereSuchSongServerCommand extends ServerCommand {
    private static final int EXPECTED_INPUT_LENGTH = 1;

    public IsThereSuchSongServerCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to see the songs option";
        }

        String songName = arguments[0];
        return serverResources.getSongsRepository().getSongByName(songName) == null ? "no" : "yes";

    }
}

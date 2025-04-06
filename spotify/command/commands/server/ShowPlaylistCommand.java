package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class ShowPlaylistCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 1;

    public ShowPlaylistCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to use the \\show playlist\\ option";
        }

        Playlist playlistToDisplay = loggedUser.getPlaylistsRepository().getById(arguments[0]);
        if (playlistToDisplay == null) {
            return "There is no such playlist!";
        }

        return playlistToDisplay.display();
    }
}

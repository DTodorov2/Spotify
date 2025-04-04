package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class RemovePlaylistCommand extends ServerCommand {
    private static final int EXPECTED_INPUT_LENGTH = 1;

    public RemovePlaylistCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        User loggedUser = getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to use the \\remove playlist\\ option";
        }

        //trqq li da pravq nqkakvi proverki za imeto na playlist-a, razlichni ot tezi v addPlaylist???

        String unsuccessfulMess = "There is not a playlist with this name in your repository!";
        String successfulMess = "The playlist is removed successfully!";

        return loggedUser.removePlaylist(arguments[0]) ? successfulMess : unsuccessfulMess;
    }
}

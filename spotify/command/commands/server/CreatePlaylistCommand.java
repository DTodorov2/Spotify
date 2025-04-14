package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class CreatePlaylistCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 1;

    public CreatePlaylistCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to use the \\create playlist\\ option";
        }

        //trqq li da pravq nqkakvi proverki za imeto na playlist-a, razlichni ot tezi v addPlaylist???

        String failMess = "There is a playlist with this name in your repository!" +
                System.lineSeparator() +
                "The new playlist is not created!";
        String successMess = "The playlist is created successfully!";

        return loggedUser.addPlaylist(arguments[0]) ? successMess : failMess;
    }
}

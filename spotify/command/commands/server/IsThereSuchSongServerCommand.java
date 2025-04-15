package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class IsThereSuchSongServerCommand extends ServerCommand {
    private static final int EXPECTED_INPUT_LENGTH = 1;

    public IsThereSuchSongServerCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to see the songs option";
        }

        String songName = arguments[0];
        List<Song> songs = serverResources.getSongsRepository().getSongByName(songName);
        if (songs.isEmpty()) {
            return "There is no such song!";
        } else if (songs.size() == 1) {
            return "yes";
        }

        return "There is more than one song with this name, please enter an artist and use the command play-by-artist!";
        //return serverResources.getSongsRepository().getSongByName(songName) == null ? "There is no such song!" : "yes";

    }
}

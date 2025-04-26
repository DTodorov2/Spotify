package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist.displayListOfSongs;

public class SearchCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 0;

    public SearchCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        if (arguments.length < 1) {
            throw new InvalidArgumentsCountException("The words are expected to be more than zero!");
        }

        boolean isUserLogged = serverResources.isUserLogged();
        if (!isUserLogged) {
            return LOGIN_REQUIRED;
        }

        List<Song> songList = serverResources.getSongsRepository().searchByWords(arguments);

        return displayListOfSongs(songList);
    }

}

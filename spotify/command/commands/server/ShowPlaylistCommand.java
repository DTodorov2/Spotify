package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.List;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.EMPTY_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist.displayListOfSongs;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class ShowPlaylistCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 1;

    public ShowPlaylistCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return LOGIN_REQUIRED;
        }

        Playlist playlistToDisplay = loggedUser.getPlaylistsRepository().getById(arguments[0]);
        if (playlistToDisplay == null) {
            return NO_SUCH_PLAYLIST;
        }
        Set<Song> songSet = playlistToDisplay.getAllSongs();
        if (songSet.isEmpty()) {
            return EMPTY_PLAYLIST;
        }

        return displayListOfSongs(songSet.stream().toList());
    }

}

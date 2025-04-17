package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_SONG;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SONG_NOT_IN_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_SONG_REMOVAL;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class RemoveSongFromPlaylistCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public RemoveSongFromPlaylistCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return LOGIN_REQUIRED;
        }

        String playlistName = arguments[0];
        String songName = arguments[1];

        Playlist playlistToAddSongTo = loggedUser.getPlaylistsRepository().getById(playlistName);
        if (playlistToAddSongTo == null) {
            return NO_SUCH_PLAYLIST;
        }

        Song song = serverResources.getSongsRepository().getSongByName(songName);
        if (song == null) {
            return NO_SUCH_SONG;
        }

        if (!playlistToAddSongTo.removeSong(song)) {
            return SONG_NOT_IN_PLAYLIST;
        }

        return SUCCESS_SONG_REMOVAL;
    }
}

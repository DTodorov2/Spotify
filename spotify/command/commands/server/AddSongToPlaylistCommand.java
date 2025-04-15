package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class AddSongToPlaylistCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public AddSongToPlaylistCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to use the \\add song to playlist\\ option";
        }

        String playlistName = arguments[0];
        String songName = arguments[1];

        Playlist playlistToAddSongTo = loggedUser.getPlaylistsRepository().getById(playlistName);
        if (playlistToAddSongTo == null) {
            return "There is no such playlist!";
        }

        Song song = serverResources.getSongsRepository().getSongByName(songName);
        if (song == null) {
            return "There is no such song in the app!";
        }

        if (!playlistToAddSongTo.addSong(song)) {
            return "This song is already in the playlist!";
        }

        return "Song: " + song.getTitle() + " is added successfully to playlist: " + playlistName;
    }
}

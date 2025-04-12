package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.List;

public class AddSongToPlaylistCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public AddSongToPlaylistCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
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

        List<Song> songsWithThisName = serverResources.getSongsRepository().getSongByName(songName);
        if (songsWithThisName.isEmpty()) {
            return "There is no such song in the app!";
        } else if (songsWithThisName.size() > 1) {
            return "There is more than one song with this title, please give the name of the artist!";
        }

        Song songToAdd = songsWithThisName.getFirst();
        if (!playlistToAddSongTo.addSong(songToAdd)) {
            return "This song is already in the playlist!";
        }

        return "Song: " + songToAdd.getTitle() + " is added successfully to playlist: " + playlistName;
    }
}

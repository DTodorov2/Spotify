package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class RemoveSongWithArtistFromPlaylistCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 3;

    public RemoveSongWithArtistFromPlaylistCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        User loggedUser = getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to use the \\remove song from playlist\\ option";
        }

        String playlistName = arguments[0];
        String songName = arguments[1];
        String artistName = arguments[2];

        Playlist playlistToAddSongTo = loggedUser.getPlaylistsRepository().getById(playlistName);
        if (playlistToAddSongTo == null) {
            return "There is no such playlist!";
        }

        Song song = serverResources.getSongsRepository().getSongByNameAndArtist(songName, artistName);
        if (song == null) {
            return "There is no such song with title: " + songName + " and artist: " + artistName;
        }

        if (!playlistToAddSongTo.removeSong(song)) {
            return "The given song is not in the playlist!";
        }

        return "Song: " + songName + " is removed successfully from playlist: " + playlistName;
    }
}

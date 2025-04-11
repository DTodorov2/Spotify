package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.nio.channels.SocketChannel;

public class PlaySongWithArtistServerCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public PlaySongWithArtistServerCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to use the \\play song\\ option";
        }

        String songName = arguments[0];
        String artistName = arguments[1];

        Song song = serverResources.getSongsRepository().getSongByNameAndArtist(songName, artistName);
        if (song == null) {
            return "There is no such song with title: " + songName + " and artist: " + artistName;
        }

        song.incrementPlayCount();
        //super.serverState.getKey().cancel(); -> tova nz dali trqq go ima vuobshte
        SocketChannel channel = (SocketChannel) serverResources.getSelectionKey().channel();
        serverResources.getExecutor().execute(new AudioServer(songName, channel));

        return "Song with name: " + songName + " will start playing shortly!";
    }
}

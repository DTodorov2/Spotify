package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.nio.channels.SocketChannel;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class PlaySongServerCommand extends ServerCommand {
    private static final int EXPECTED_INPUT_LENGTH = 1;

    public PlaySongServerCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException, InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser == null) {
            return "You must be logged in in order to use the \\play song\\ option";
        }

        return handleSongPlayRequest();
    }

    private String handleSongPlayRequest() {
        String songName = arguments[0];
        List<Song> songsByName = serverResources.getSongsRepository().getSongByName(songName);
        if (songsByName.isEmpty()) {
            return "No such song in the system!";
        } else if (songsByName.size() > 1) {
            return "There is more than one song with such name, please select the option to enter an artist!";
        } else {
            Song song = songsByName.getFirst();
            song.incrementPlayCount();

            SocketChannel channel = (SocketChannel) serverResources.getSelectionKey().channel();
            AudioServer audioServer = new AudioServer(songName, channel);

            serverResources.getSelectionKey().cancel();
            serverResources.getExecutor().execute(audioServer);
        }
        return "";
    }
}

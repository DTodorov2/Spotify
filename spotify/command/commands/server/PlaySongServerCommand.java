package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.nio.channels.SocketChannel;
import java.util.Collection;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.EMPTY_MESSAGE;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_SONG;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

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
            return LOGIN_REQUIRED;
        }

        return handleSongPlayRequest(loggedUser);
    }

    private String handleSongPlayRequest(User loggedUser) {
        checkArgumentNotNull(loggedUser, "logged user");
        String songName = arguments[0];

        Song song = serverResources.getSongsRepository().getSongByName(songName);
        if (song == null) {
            return NO_SUCH_SONG;
        } else {
            song.incrementPlayCount();
            incrementPlayingsCountInPlaylists(songName);

            SocketChannel channel = (SocketChannel) serverResources.getSelectionKey().channel();
            AudioServer audioServer = new AudioServer(songName, channel, loggedUser);

            serverResources.getSelectionKey().cancel();
            serverResources.getExecutor().execute(audioServer);
        }
        return EMPTY_MESSAGE;
    }

    private void incrementPlayingsCountInPlaylists(String songName) {
        validateString(songName, "song name");
        Collection<User> users = serverResources.getUsersRepository().getMap().values();
        for (User user : users) {
            Collection<Playlist> playlists =  user.getPlaylistsRepository().getMap().values();
            for (Playlist playlist : playlists) {
                if (playlist.contains(songName)) {
                    playlist.incrementPlayingsCountOf(songName);
                }
            }
        }
    }

}

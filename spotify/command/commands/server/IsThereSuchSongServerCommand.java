package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_SONG;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SONG_EXISTS;
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
            return LOGIN_REQUIRED;
        }

        String songName = arguments[0];
        Song song = serverResources.getSongsRepository().getSongByName(songName);
        if (song == null) {
            return NO_SUCH_SONG;
        }

        return SONG_EXISTS;
    }
}

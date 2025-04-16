package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.MORE_THAN_ZERO_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class TopSongsCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 1;

    public TopSongsCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        boolean isUserLogged = serverResources.isUserLogged();
        if (!isUserLogged) {
            return LOGIN_REQUIRED;
        }

        int argAsInt = Integer.parseInt(arguments[0]);
        if (argAsInt < 1) {
            return MORE_THAN_ZERO_REQUIRED;
        }
        List<Song> songList = serverResources.getSongsRepository().getTopStatistics(argAsInt);

        return displayListOfSongs(songList);
    }
}

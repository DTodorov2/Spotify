package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class SearchCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 1;

    public SearchCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        boolean isUserLogged = serverResources.isUserLogged();
        if (!isUserLogged) {
            return "You must be logged in in order to use the \\search\\ option";
        }

        List<Song> songList = serverResources.getSongsRepository().searchByWords(arguments);

        return displayListOfSongs(songList);
    }

}

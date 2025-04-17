package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class ShowAllCommandsCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 0;

    public ShowAllCommandsCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException, InvalidArgumentsCountException {
        return "Available Commands:" + System.lineSeparator() +
                "register <email> <password> -> register the user into the system" + System.lineSeparator() +
                "login <email> <password> -> login the user" + System.lineSeparator() +
                "disconnect -> disconnects the user from the system" + System.lineSeparator() +
                "search <words> -> returns all songs whose titles or artist names contain the searched word/words" +
                System.lineSeparator() +
                "top <number> -> returns a list of the number most listened-to songs, sorted in descending order." +
                System.lineSeparator() +
                "create-playlist <name_of_the_playlist>" + System.lineSeparator() +
                "add-song-to <name_of_the_playlist> <song>" + System.lineSeparator() +
                "show-playlist <name_of_the_playlist>" + System.lineSeparator() +
                "play <song>" + System.lineSeparator() +
                "stop -> stops the streaming song";
    }
}

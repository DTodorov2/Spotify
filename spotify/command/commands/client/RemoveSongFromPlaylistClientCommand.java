package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;

public class RemoveSongFromPlaylistClientCommand extends ClientCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public RemoveSongFromPlaylistClientCommand(String userInput, String[] arguments, ClientResources clientResources) {
        super(userInput, arguments, EXPECTED_INPUT_LENGTH, clientResources);
    }

}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;

public class CreatePlaylistClientCommand extends ClientCommand {

    private static final int EXPECTED_INPUT_LENGTH = 1;

    public CreatePlaylistClientCommand(String userInput, String[] arguments, ClientResources clientResources) {
        super(userInput, arguments, EXPECTED_INPUT_LENGTH, clientResources);
    }

}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;

public class RemoveSongWithArtistFromPlaylistClientCommand extends ClientCommand {

    private static final int EXPECTED_INPUT_LENGTH = 3;

    public RemoveSongWithArtistFromPlaylistClientCommand(String userInput, String[] arguments, ClientResources clientResources) {
        super(userInput, arguments, EXPECTED_INPUT_LENGTH, clientResources);
    }

}

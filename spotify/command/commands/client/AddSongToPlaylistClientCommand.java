package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;

public class AddSongToPlaylistClientCommand extends ClientCommand {

    public AddSongToPlaylistClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

}

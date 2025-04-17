package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;

public class RemoveSongFromPlaylistClientCommand extends ClientCommand {

    public RemoveSongFromPlaylistClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

}

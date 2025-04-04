package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class AddSongWithArtistToPlaylist extends ServerCommand {
    public AddSongWithArtistToPlaylist(String[] arguments, int expectedCountArguments, ServerResources serverResources) {
        super(arguments, expectedCountArguments, serverResources);
    }

    @Override
    public String execute() {
        return "";
    }
}

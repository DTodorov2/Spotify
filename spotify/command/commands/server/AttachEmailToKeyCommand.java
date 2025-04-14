package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.nio.channels.SocketChannel;

public class AttachEmailToKeyCommand extends ServerCommand {
    private static final int EXPECTED_INPUT_LENGTH = 1;

    public AttachEmailToKeyCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        String emailToAttach = arguments[0];
        serverResources.getSelectionKey().attach(emailToAttach);
        return "READY";
    }
}

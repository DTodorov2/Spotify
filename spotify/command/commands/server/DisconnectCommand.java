package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulChannelClosingException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class DisconnectCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 0;

    public DisconnectCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        SelectionKey key = serverResources.getSelectionKey();
        closeClientChannel((SocketChannel) key.channel(), key);

        return "The user with email: " + key.attachment() + " disconnected successfully!";
    }

    private void closeClientChannel(SocketChannel channel, SelectionKey key) {
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser != null) {
            loggedUser.setLoggedIn(false);
            //dolniq method e ako srteam-va muzika, da q spra
            //setLogoutState(loggedUser, serverState);
        }

        try {
            channel.close();
        } catch (IOException e) {
            throw new UnsuccessfulChannelClosingException("Unable to close the client's socket channel!", e);
        }

        key.cancel();
    }
}

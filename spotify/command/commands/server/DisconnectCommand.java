package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulChannelClosingException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.EMPTY_MESSAGE;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class DisconnectCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 0;

    public DisconnectCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException, InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        SelectionKey key = serverResources.getSelectionKey();
        closeClientChannel((SocketChannel) key.channel(), key);

        return EMPTY_MESSAGE;
    }

    private void closeClientChannel(SocketChannel channel, SelectionKey key) {
        User loggedUser = serverResources.getLoggedUser();
        if (loggedUser != null) {
            loggedUser.setLoggedIn(false);
            serverResources.getSelectionKey().attach(null);
            AudioServer audioServer =  loggedUser.getAudioServer();
            if (audioServer != null) {
                audioServer.setIsStreaming(false);
            }
        }

        try {
            channel.close();
        } catch (IOException e) {
            throw new UnsuccessfulChannelClosingException("Unable to close the client's socket channel!", e);
        }

        key.cancel();
    }
}

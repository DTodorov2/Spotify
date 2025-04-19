package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommandFactory;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.PlaySongClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ShowAllCommandsClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_COMMAND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientCommandInvokerTest {

    private ClientResources clientResources;
    private ChannelDataWriter channelDataWriter;
    private ChannelDataLoader channelDataLoader;
    private ClientCommandInvoker clientCommandInvoker;

    @BeforeEach
    void setUp() {

        clientResources = mock(ClientResources.class);
        SocketChannel mockSocket = mock(SocketChannel.class);
        when(clientResources.getClientChannel()).thenReturn(mockSocket);
        channelDataWriter = mock(ChannelDataWriter.class);
        channelDataLoader = mock(ChannelDataLoader.class);

        clientCommandInvoker = new ClientCommandInvoker(clientResources);
    }

    @Test
    void testExecuteCommandWithInvalidCommand() throws ChannelCommunicationException {
        String result = clientCommandInvoker.executeCommand("unknown-command", new String[]{"testSong"});

        assertEquals(NO_SUCH_COMMAND, result);
    }

    @Test
    void testExecuteCommandWithCommandThatThrowsException() throws ChannelCommunicationException {
        String response = clientCommandInvoker.executeCommand("show-all-commands", new String[]{});
        assertEquals("", response);
    }

}

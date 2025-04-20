package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.EMPTY_MESSAGE;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_COMMAND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientCommandInvokerTest {

    private ClientCommandInvoker clientCommandInvoker;

    @BeforeEach
    void setUp() {

        ClientResources clientResources = mock(ClientResources.class);
        SocketChannel mockSocket = mock(SocketChannel.class);
        when(clientResources.getClientChannel()).thenReturn(mockSocket);

        clientCommandInvoker = new ClientCommandInvoker(clientResources);
    }

    @Test
    void testExecuteCommandWithInvalidCommand() {
        try {
            String result = clientCommandInvoker.executeCommand("random-cmd", new String[]{"testSong"});
            assertEquals(NO_SUCH_COMMAND, result);
        } catch (ChannelCommunicationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testExecuteCommandWithValidCommand() {
        try {
            String response = clientCommandInvoker.executeCommand("show-all-commands", new String[]{});
            assertEquals(EMPTY_MESSAGE, response);
        } catch (ChannelCommunicationException e) {
            fail(e.getMessage());
        }
    }

}

package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.RemovePlaylistClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RemovePlaylistClientCommandTest {

    private final String userInput = "input";
    private ChannelDataWriter channelDataWriter;
    private ChannelDataLoader channelDataLoader;
    private ClientResources mockClientResources;

    @BeforeEach
    void setUp() {
        mockClientResources = mock(ClientResources.class);
        SocketChannel mockSocket = mock(SocketChannel.class);
        when(mockClientResources.getClientChannel()).thenReturn(mockSocket);

        channelDataWriter = new ChannelDataWriter(mockSocket);
        channelDataLoader = new ChannelDataLoader(mockSocket);
    }

    @Test
    void testExecuteWithNullArgs() {

        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemovePlaylistClientCommand(null, mockClientResources,
                        channelDataWriter, channelDataLoader), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        assertDoesNotThrow(() -> new RemovePlaylistClientCommand(userInput, mockClientResources,
                channelDataWriter, channelDataLoader), "The object should be created successfully!");
    }

    @Test
    void testExecuteWithNullServerResourcesArg() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemovePlaylistClientCommand(userInput, null,
                        channelDataWriter, channelDataLoader), errMess);
    }

    @Test
    void testExecuteWithNullChannelDataWriter() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null channel data writer argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemovePlaylistClientCommand(userInput, mockClientResources,
                        null, channelDataLoader), errMess);
    }

    @Test
    void testExecuteWithNullChannelDataLoader() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null channel data loader argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemovePlaylistClientCommand(userInput, mockClientResources,
                        channelDataWriter, null), errMess);
    }

}

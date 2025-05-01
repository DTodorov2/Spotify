package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.DisconnectCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulChannelClosingException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DisconnectCommandTest {

    private static final String TEST_PARAMETER = "parameter";

    private ServerResources mockServerResources;
    private SelectionKey mockSelectionKey;
    private SocketChannel mockSocketChannel;
    private User mockUser;
    private AudioServer mockAudioServer;
    private DisconnectCommand command;

    @BeforeEach
    void setUp() {
        mockServerResources = mock(ServerResources.class);
        mockSelectionKey = mock(SelectionKey.class);
        mockSocketChannel = mock(SocketChannel.class);
        mockUser = mock(User.class);
        mockAudioServer = mock(AudioServer.class);

        when(mockServerResources.getSelectionKey()).thenReturn(mockSelectionKey);
        when(mockSelectionKey.channel()).thenReturn(mockSocketChannel);
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockUser.getAudioServer()).thenReturn(mockAudioServer);

        command = new DisconnectCommand(new String[]{}, mockServerResources);
    }

    @Test
    void testExecuteWithInvalidArgsCount() {
        command.setArguments(new String[]{TEST_PARAMETER});

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new DisconnectCommand(null, mockServerResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {TEST_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new DisconnectCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArgument() {
        String[] arguments = {TEST_PARAMETER};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new DisconnectCommand(arguments, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArgument() {
        String[] arguments = {TEST_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new DisconnectCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testExecuteSuccessful() {
        try {
            String result = command.execute();

            assertEquals("", result, "Successful command execution is expected!");

            verify(mockSocketChannel).close();
            verify(mockSelectionKey).cancel();
            verify(mockUser).setLoggedIn(false);
            verify(mockServerResources, times(2)).getSelectionKey();
            verify(mockUser).getAudioServer();
            verify(mockAudioServer).setIsStreaming(false);
        } catch (IOException | ChannelCommunicationException | InvalidArgumentsCountException e) {
            fail("The disconnect command could not execute! Reason: " + e.getMessage());
        }

    }

    @Test
    void testExecuteWithThrownChannelClosingException() {
        try {
            doThrow(new IOException("Error closing channel")).when(mockSocketChannel).close();
        } catch (IOException e) {
            fail("The disconnect command could not execute! Reason: " + e.getMessage());
        }
        String errMess = "UnsuccessfulChannelClosingException is expected to be thrown " +
                "when the channel cannot be closed!";
        assertThrows(UnsuccessfulChannelClosingException.class, () -> command.execute(), errMess);
    }

}

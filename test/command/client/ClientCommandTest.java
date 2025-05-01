package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.START_SONG_PLAYING;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientCommandTest {

    private static final String PLAY_COMMAND = "play StandStill";
    private static ClientCommand command;

    private static ChannelDataWriter mockWriter;
    private static ChannelDataLoader mockLoader;
    private static ClientResources mockResources;

    @BeforeAll
    static void setUp() {
        SocketChannel mockSocketChannel = mock(SocketChannel.class);
        ClientResources mockClientResources = mock(ClientResources.class);
        when(mockClientResources.getClientChannel()).thenReturn(mockSocketChannel);

        mockResources = mock(ClientResources.class);
        SocketChannel mockSocket = mock(SocketChannel.class);
        when(mockResources.getClientChannel()).thenReturn(mockSocket);

        mockWriter = mock(ChannelDataWriter.class);
        mockLoader = mock(ChannelDataLoader.class);
        command = new ClientCommandStubImpl(PLAY_COMMAND, mockResources, mockLoader, mockWriter);
    }

    @Test
    void testGetArgsWithValidInput() {
        String[] args = command.getArgs();
        assertArrayEquals(new String[]{"play", "StandStill"}, args,
                "The two arrays are expected to be the same!");
    }

    @Test
    void testCreatingClientCommandWithNullUserInput() {
        String errMess = "IllegalArgumentException is expected to be thrown, " +
                "when creating a client command with null user input!";

        assertThrows(IllegalArgumentException.class, () ->
                new ClientCommandStubImpl(null, mockResources, mockLoader, mockWriter), errMess);
    }

    @Test
    void testIsCorrectArgsCountWithCorrectCount() {
        assertTrue(command.isCorrectArgsCount(2),
                "IsCorrectArgsCount is expected to return true, when the count of the args is correct");
    }

    @Test
    void testIsCorrectArgsCountWithIncorrectCount() {
        assertFalse(command.isCorrectArgsCount(3),
                "IsCorrectArgsCount is expected to return false, when the count of the args is incorrect");
    }

    @Test
    void testExecuteWithValidCommand() {
        String expectedResponse = START_SONG_PLAYING;

        String result = null;
        try {
            when(mockLoader.loadData()).thenReturn(expectedResponse);
            result = command.execute();
        } catch (DeserializationDataException | ChannelCommunicationException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }

        assertEquals(expectedResponse, result,
                "The returned result and the expected result must be equal, but are not!");
    }

    @Test
    void testExecuteThrowsChannelCommunicationException() {
        try {
            when(mockLoader.loadData()).thenThrow(new DeserializationDataException("Deserialization error"));
        } catch (DeserializationDataException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }

        ClientCommand command = new ClientCommandStubImpl(PLAY_COMMAND, mockResources, mockLoader, mockWriter);

        String errMess = "ChannelCommunicationException is expected to be thrown " +
                "when the data from the channel cannot be read!";

        assertThrows(ChannelCommunicationException.class, command::execute, errMess);
    }

}

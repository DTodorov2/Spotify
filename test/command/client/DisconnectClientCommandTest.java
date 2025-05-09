package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.DisconnectClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_DISCONNECT;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.getWrongParametersMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DisconnectClientCommandTest {

    private static final String DISCONNECT_COMMAND = "disconnect";
    private ClientResources mockClientResources;

    private DisconnectClientCommand command;
    private ChannelDataWriter mockWriter;

    @BeforeEach
    void setUp() {
        mockClientResources = mock(ClientResources.class);
        SocketChannel mockSocket = mock(SocketChannel.class);
        when(mockClientResources.getClientChannel()).thenReturn(mockSocket);

        mockWriter = mock(ChannelDataWriter.class);
        ChannelDataLoader mockLoader = mock(ChannelDataLoader.class);
        command = new DisconnectClientCommand(DISCONNECT_COMMAND, mockClientResources, mockWriter, mockLoader);
    }

    @Test
    void testExecuteWithValidArgs() {
        AudioClient mockAudioClient = mock(AudioClient.class);

        when(mockClientResources.isStreaming()).thenReturn(true);
        when(mockClientResources.getAudioClient()).thenReturn(mockAudioClient);

        try {
            String result = command.execute();
            assertEquals(SUCCESS_DISCONNECT, result, "Expected to return the success disconnect message.");
        } catch (ChannelCommunicationException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }

        verify(mockAudioClient).stopStreaming();
        verify(mockClientResources).setClientLogged(false);
    }

    @Test
    void testExecuteWithInvalidArgsCount() {
        String wrongUserInput = DISCONNECT_COMMAND + " parameter";
        command.setUserInput(wrongUserInput);

        try {
            String result = command.execute();
            assertEquals(getWrongParametersMessage(1, 2), result,
                    "Expected error message for wrong argument count.");
        } catch (ChannelCommunicationException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }
    }

    @Test
    void testExecuteThrowsChannelCommunicationException() {
        try {
            doThrow(new SerializationDataException("Serialization error"))
                    .when(mockWriter).saveData(anyString());
        } catch (SerializationDataException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }

        command.setChannelDataWriter(mockWriter);

        String errMess = "ChannelCommunicationException is expected to be thrown " +
                "when the data from the channel cannot be saved!";
        assertThrows(ChannelCommunicationException.class, command::execute, errMess);
    }

    @Test
    void testSetChannelDataWriterWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> command.setChannelDataWriter(null),
                "The channel data writer cannot be set with null value!");
    }

}

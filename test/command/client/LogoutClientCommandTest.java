package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.LogoutClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogoutClientCommandTest {

    private static final String LOGOUT_COMMAND = "logout";

    private ClientResources mockClientResources;
    private AudioClient mockAudioClient;
    private ChannelDataLoader mockLoader;
    private LogoutClientCommand command;

    @BeforeEach
    void setUp() {
        mockClientResources = mock(ClientResources.class);
        SocketChannel mockSocket = mock(SocketChannel.class);
        when(mockClientResources.getClientChannel()).thenReturn(mockSocket);

        mockAudioClient = mock(AudioClient.class);
        when(mockClientResources.getAudioClient()).thenReturn(mockAudioClient);

        ChannelDataWriter channelDataWriter = new ChannelDataWriter(mockSocket);
        mockLoader = mock(ChannelDataLoader.class);
        command = new LogoutClientCommand(LOGOUT_COMMAND, mockClientResources, channelDataWriter, mockLoader);

    }

    @Test
    void testExecuteWithValidArguments() {
        String result = commandExecute();
        assertNotNull(result, "Successful command execution is expected!");

        verify(mockClientResources).setClientLogged(false);
        verify(mockAudioClient).stopStreaming();
    }

    @Test
    void testExecuteWithInvalidArguments() {
        String userInput = LOGOUT_COMMAND + " parameter";
        command.setUserInput(userInput);

        String result = commandExecute();
        assertNotNull(result, "Unsuccessful command execution is expected!");

        verify(mockClientResources, never()).setClientLogged(false);
        verify(mockAudioClient, never()).stopStreaming();
    }

    private String commandExecute() {
        try {
            when(mockLoader.loadData()).thenReturn("text");
            return command.execute();
        } catch (DeserializationDataException | ChannelCommunicationException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }

        return null;
    }

}

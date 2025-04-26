package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.StopSongClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_STREAMING_SONG;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.STOP_SONG_PLAYING;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.getWrongParametersMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StopSongClientCommandTest {

    private static final String STOP_COMMAND = "stop";

    private ClientResources mockClientResources;
    private AudioClient mockAudioClient;
    private StopSongClientCommand command;

    @BeforeEach
    void setUp() {
        mockClientResources = mock(ClientResources.class);
        SocketChannel mockSocket = mock(SocketChannel.class);
        when(mockClientResources.getClientChannel()).thenReturn(mockSocket);
        when(mockClientResources.isClientLogged()).thenReturn(true);

        mockAudioClient = mock(AudioClient.class);

        ChannelDataWriter mockWriter = mock(ChannelDataWriter.class);
        ChannelDataLoader mockLoader = mock(ChannelDataLoader.class);
        command = new StopSongClientCommand(STOP_COMMAND, mockClientResources, mockWriter, mockLoader);

    }

    @Test
    void testExecuteWithInvalidArguments() {
        String commandText = STOP_COMMAND + " parameter";
        command.setUserInput(commandText);

        String result = command.execute();

        assertEquals(getWrongParametersMessage(1, 2), result,
                "Message for wrong number of arguments is expected to be shown.");
    }

    @Test
    void testExecuteWhenClientNotLogged() {
        when(mockClientResources.isClientLogged()).thenReturn(false);

        String result = command.execute();

        assertEquals(LOGIN_REQUIRED, result, "Required login if client is not logged in.");
    }

    @Test
    void testExecuteWhenSongNotStreaming() {
        when(mockClientResources.isStreaming()).thenReturn(false);

        String result = command.execute();

        assertEquals(NO_STREAMING_SONG, result,
                "Message that a song is currently streaming is expected to be shown!");
    }

    @Test
    void testExecuteAnotherSongStreaming() {
        when(mockClientResources.isStreaming()).thenReturn(true);
        when(mockClientResources.getAudioClient()).thenReturn(mockAudioClient);

        String result = command.execute();
        assertEquals(STOP_SONG_PLAYING, result, "Stop song streaming message is expected to be shown!");

        verify(mockAudioClient).stopStreaming();
    }

}

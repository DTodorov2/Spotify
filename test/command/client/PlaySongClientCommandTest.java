package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.PlaySongClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.MarshallingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.spotify.command.CommandInvoker.PLAY;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.MUSIC_STOP_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SONG_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlaySongClientCommandTest {

    private ClientResources clientResources;
    private ChannelDataLoader channelDataLoader;
    private PlaySongClientCommand playSongClientCommand;

    @BeforeEach
    void setUp() {
        clientResources = mock(ClientResources.class);
        ChannelDataWriter channelDataWriter = mock(ChannelDataWriter.class);
        channelDataLoader = mock(ChannelDataLoader.class);

        playSongClientCommand = new PlaySongClientCommand("play StandStill",
                clientResources,
                channelDataWriter,
                channelDataLoader);
    }

    @Test
    void testExecuteWithValidArgs() {
        try {
            when(clientResources.isStreaming()).thenReturn(false);
            when(channelDataLoader.loadData()).thenReturn(SONG_EXISTS);

            assertThrows(ChannelCommunicationException.class, () -> playSongClientCommand.execute(),
                    "ChannelCommunicationException is expected to be thrown, " +
                            "when a socket channel for the audio client cannot be created!");
        } catch (MarshallingException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }

    }

    @Test
    void testExecuteWhenSongIsAlreadyPlaying() {
        when(clientResources.isStreaming()).thenReturn(true);

        try {
            String result = playSongClientCommand.execute();
            assertEquals(MUSIC_STOP_REQUIRED, result, MUSIC_STOP_REQUIRED + " message is expected to be shown!");
        } catch (ChannelCommunicationException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }
    }

    @Test
    void testExecuteWithWrongArguments() {
        playSongClientCommand.setUserInput(PLAY);

        try {
            String result = playSongClientCommand.execute();
            assertTrue(result.contains("Wrong number of parameters"), "Wrong args count message is expected!");
        } catch (ChannelCommunicationException e) {
            fail("Could not execute the test! Reason: " + e.getMessage());
        }
    }

}

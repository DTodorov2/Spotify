package bg.sofia.uni.fmi.mjt.server.audio;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AudioServerTest {

    private SocketChannel mockChannel;
    private User mockUser;
    private AudioInputStream mockAudioInputStream;
    private ChannelDataLoader mockChannelDataLoader;
    private ChannelDataWriter mockChannelDataWriter;

    @BeforeEach
    public void setUp() {
        mockChannel = mock(SocketChannel.class);
        mockUser = mock(User.class);
        mockAudioInputStream = mock(AudioInputStream.class);
        mockChannelDataLoader = mock(ChannelDataLoader.class);
        mockChannelDataWriter = mock(ChannelDataWriter.class);

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        AudioFormat audioFormat = new AudioFormat(encoding,
                44100.0f,
                16,
                2,
                4,
                44100.0f,
                true);

        when(mockAudioInputStream.getFormat()).thenReturn(audioFormat);
        when(mockChannelDataWriter.getChannel()).thenReturn(mockChannel);
        try {
            when(mockChannelDataLoader.loadBytes()).thenReturn("stop".getBytes(StandardCharsets.UTF_8));
        } catch (DeserializationDataException e) {
            fail("The set up cannot be executed! Reason: " + e.getMessage());
        }
    }

    @Test
    public void testRunMethod() {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class)) {
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(File.class)))
                    .thenReturn(mockAudioInputStream);

            AudioServer server = new AudioServer("testSong", mockChannelDataWriter,
                    mockChannelDataLoader, mockUser);

            server.run();
        }

        try {
            verify(mockChannel, times(1)).write((ByteBuffer) any());
        } catch (IOException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }
    }
}

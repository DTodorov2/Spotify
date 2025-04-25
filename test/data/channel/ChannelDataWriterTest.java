package bg.sofia.uni.fmi.mjt.data.channel;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.WritingToChannelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChannelDataWriterTest {

    private ChannelDataWriter channelWriter;
    private SocketChannel mockChannel;

    @BeforeEach
    void setUp() {
        mockChannel = mock(SocketChannel.class);
        channelWriter = new ChannelDataWriter(mockChannel);
    }

    @Test
    void testSaveDataSuccessfullyWritesToChannel() {
        try {
            byte[] data = "testData".getBytes();
            channelWriter.saveData(data);

            verify(mockChannel, times(1)).write(any(ByteBuffer.class));
        } catch (IOException | SerializationDataException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }

    }

    @Test
    void testSaveDataWithNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> channelWriter.saveData((byte[]) null),
                "IllegalArgumentException is expected to be thrown when null argument is passed!");
    }

    @Test
    void testSaveDataWithByteArrayThrowsWritingToChannelException() {
        try {
            doThrow(new IOException("IO")).when(mockChannel).write(any(ByteBuffer.class));
        } catch (IOException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }

        byte[] data = "data".getBytes();
        assertThrows(WritingToChannelException.class, () -> channelWriter.saveData(data),
                "WritingToChannelException is expected to be thrown when IOException is thrown!");
    }

    @Test
    void testSaveDataWithStringThrowsWritingToChannelException() {
        try {
            doThrow(new IOException("IO")).when(mockChannel).write(any(ByteBuffer.class));
        } catch (IOException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }

        assertThrows(WritingToChannelException.class, () -> channelWriter.saveData("data"),
                "WritingToChannelException is expected to be thrown when IOException is thrown!");
    }

}

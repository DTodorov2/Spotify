package bg.sofia.uni.fmi.mjt.data.channel;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ReadingFromChannelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChannelDataLoaderTest {

    private ChannelDataLoader loader;
    private SocketChannel mockChannel;

    @BeforeEach
    void setUp() {
        mockChannel = mock(SocketChannel.class);
        loader = new ChannelDataLoader(mockChannel);

        try {
            when(mockChannel.read(any(ByteBuffer.class))).thenThrow(new IOException("IOException error"));
        } catch (IOException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }
    }

    @Test
    void testLoadBytesReturnsNullWhenEndOfStream() {
        try {
            when(mockChannel.read(any(ByteBuffer.class))).thenReturn(-1);

            byte[] result = loader.loadBytes();
            assertNull(result, "Expected null when read returns -1");
        } catch (DeserializationDataException | IOException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }

    }

    @Test
    void testLoadBytesThrowsWhenIOExceptionOccurs() {
        String mess = "ReadingFromChannelException is expected to be thrown when IOException occurs in loadBytes!";
        assertThrows(ReadingFromChannelException.class, loader::loadBytes, mess);
    }

    @Test
    void testLoadDataThrowsWhenIOExceptionOccurs() {
        String mess = "ReadingFromChannelException is expected to be thrown when IOException occurs in loadData!";
        assertThrows(ReadingFromChannelException.class, loader::loadData, mess);
    }

}

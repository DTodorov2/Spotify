package bg.sofia.uni.fmi.mjt.data.channel;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ReadingFromChannelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    }

    @Test
    void testLoadBytesReturnsNullWhenEndOfStream() throws Exception {
        when(mockChannel.read(any(ByteBuffer.class))).thenReturn(-1);

        byte[] result = loader.loadBytes();

        assertNull(result, "Expected null when read returns -1 (EOF)");
    }

    @Test
    void testLoadBytesThrowsWhenIOExceptionOccurs() throws Exception {
        when(mockChannel.read(any(ByteBuffer.class))).thenThrow(new IOException("fail"));

        assertThrows(ReadingFromChannelException.class, loader::loadBytes);
    }

    @Test
    void testLoadDataThrowsWhenIOExceptionOccurs() throws Exception {
        when(mockChannel.read(any(ByteBuffer.class))).thenThrow(new IOException("fail"));

        assertThrows(ReadingFromChannelException.class, loader::loadData);
    }

}

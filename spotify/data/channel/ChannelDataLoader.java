package bg.sofia.uni.fmi.mjt.spotify.data.channel;

import bg.sofia.uni.fmi.mjt.spotify.data.DataDeserializer;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ReadingFromChannelException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class ChannelDataLoader implements DataDeserializer {

    private final ByteBuffer buffer;
    private static final int BUFFER_CAPACITY = 4096;

    private SocketChannel channel;

    public ChannelDataLoader(SocketChannel channel) {
        checkArgumentNotNull(channel, "channel");

        buffer = ByteBuffer.allocateDirect(BUFFER_CAPACITY);
        this.channel = channel;
    }

    public void setChannel(SocketChannel channel) {
        checkArgumentNotNull(channel, "new socket channel");
        this.channel = channel;
    }

    @Override
    public String loadData() throws DeserializationDataException {
        StringBuilder message = new StringBuilder();
        buffer.clear();
        while (true) {
            try {
                int bytesRead = channel.read(buffer);

                if (bytesRead < 1) {
                    break;
                }

                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);

                message.append(new String(data, StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new ReadingFromChannelException("Unable to read from channel!", e);
            }
        }

        return message.toString();
    }

    public byte[] loadBytes() throws DeserializationDataException {
        buffer.clear();

        try {
            int bytesRead = channel.read(buffer);
            if (bytesRead < 0) {
                return null;
            }
            buffer.flip();
            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);
            return byteArray;
        } catch (IOException e) {
            throw new ReadingFromChannelException("Unable to read from channel!", e);
        }
    }

}

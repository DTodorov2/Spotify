package bg.sofia.uni.fmi.mjt.spotify.data.channel;

import bg.sofia.uni.fmi.mjt.spotify.data.DataSerializer;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.WritingToChannelException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class ChannelDataWriter implements DataSerializer {

    private final ByteBuffer buffer;
    private static final int BUFFER_CAPACITY = 4096;
    private SocketChannel channel;

    public ChannelDataWriter(SocketChannel channel) {
        checkArgumentNotNull(channel, "channel");

        buffer = ByteBuffer.allocateDirect(BUFFER_CAPACITY);
        this.channel = channel;
    }

    public void setChannel(SocketChannel channel) {
        checkArgumentNotNull(channel, "new socket channel");
        this.channel = channel;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    @Override
    public void saveData(String data, boolean toAppend) throws SerializationDataException {
        validateString(data, "data to write to channel");

        byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
        buffer.clear();
        buffer.put(byteArray);
        buffer.flip();

        try {
            channel.write(buffer);
        } catch (IOException e) {
            throw new WritingToChannelException("Unable to write to channel!", e);
        }
    }

    public void saveData(String data) throws SerializationDataException {
        validateString(data, "data to write to channel");
        saveData(data, false);
    }

    public void saveData(byte[] bytes) throws SerializationDataException {
        checkArgumentNotNull(bytes, "byte array");

        buffer.clear();
        buffer.put(bytes);
        buffer.flip();

        try {
            channel.write(buffer);
        } catch (IOException e) {
            throw new WritingToChannelException("Unable to write to channel!", e);
        }
    }

}

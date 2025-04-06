package bg.sofia.uni.fmi.mjt.spotify.data.channel;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.DataDeserializer;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ReadingFromChannelException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class ChannelDataLoader implements DataDeserializer {

    private final ClientResources clientResources;

    public ChannelDataLoader(ClientResources clientResources) {
        checkArgumentNotNull(clientResources, "client resources");

        this.clientResources = clientResources;
    }

    @Override
    public String loadData() throws DeserializationException {
        StringBuilder message = new StringBuilder();
        ByteBuffer buffer = clientResources.getBuffer();
        while (true) {
            try {
                int bytesRead = clientResources.getClientChannel().read(buffer);

                if (bytesRead == -1) {
                    break;
                }

                if (bytesRead == 0) {
                    continue;
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
}

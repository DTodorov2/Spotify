package bg.sofia.uni.fmi.mjt.spotify.data.channel;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.DataSerializer;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.WritingToChannelException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class ChannelDataWriter implements DataSerializer {

    private final ClientResources clientResources;

    public ChannelDataWriter(ClientResources clientResources) {
        checkArgumentNotNull(clientResources, "client resources");

        this.clientResources = clientResources;
    }

    @Override
    public void saveData(String data, boolean toAppend) throws SerializationException {
        byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
        clientResources.getBuffer().clear();
        clientResources.getBuffer().put(byteArray);
        clientResources.getBuffer().flip();

        try {
            clientResources.getClientChannel().write(clientResources.getBuffer());
        } catch (IOException e) {
            throw new WritingToChannelException("Unable to write to channel!", e);
        }
    }
}

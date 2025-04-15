package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

public class DisconnectClientCommand extends ClientCommand {

    private static final int EXPECTED_NUMBER_ARGS = 1;

    public DisconnectClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        try {
            channelDataWriter.saveData("disconnect");
        } catch (SerializationDataException e) {
            throw new ChannelCommunicationException("Unable to communicate with the server!", e);
        }

        if (isCorrectArgsCount(EXPECTED_NUMBER_ARGS)) {
            clientResources.setClientLogged(false);
            if (clientResources.isStreaming()) {
                AudioClient audioClient =  clientResources.getAudioClient();
                stopStreaming(audioClient);
            }
        }
        return "You disconnected successfully!";

    }
}

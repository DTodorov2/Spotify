package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public class LogoutClientCommand extends ClientCommand {

    private static final int EXPECTED_NUMBER_ARGS = 1;

    public LogoutClientCommand(String userInput,
                               ClientResources clientResources,
                               ChannelDataWriter channelDataWriter,
                               ChannelDataLoader channelDataLoader) {

        super(userInput, clientResources, channelDataWriter, channelDataLoader);

    }

    @Override
    public String execute() throws ChannelCommunicationException {
        String response = super.execute();

        if (isCorrectArgsCount(EXPECTED_NUMBER_ARGS)) {
            clientResources.setClientLogged(false);
            AudioClient audioClient =  clientResources.getAudioClient();
            if (audioClient != null) {
                audioClient.stopStreaming();
            }
        }

        return response;
    }

}

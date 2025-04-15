package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelConnectionException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.MarshallingException;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class PlaySongClientCommand extends ClientCommand {

    private static final int EXPECTED_ARGS_COUNT = 2;

    public PlaySongClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        if (!clientResources.isClientLogged()) {
            return "The user must be logged in in order to play a song!";
        }

        if (isCorrectArgsCount(EXPECTED_ARGS_COUNT)) {
            if (clientResources.isStreaming()) {
                return "You must stop the current music in order to play a new one!";
            }

            try {
                arguments = getArgs();
                String songName = arguments[1];
                channelDataWriter.saveData("check-song " + arguments[1]);
                String response = channelDataLoader.loadData();
                if (response.equals("yes")) {
                    AudioClient audioClient = new AudioClient(songName, clientResources);
                    attachToKey(audioClient);
                    clientResources.setAudioClient(audioClient);
                    clientResources.getExecutor().execute(audioClient);
                    return "Song with name: " + songName + " will start playing shortly!";
                } else {
                    return response;
                }
            } catch (ChannelConnectionException | MarshallingException e) {
                throw new ChannelCommunicationException("The server could not retrieve the song!", e);
            }
        }
        return "Wrong number of parameters! The count of the arguments is expected to be " +
                EXPECTED_ARGS_COUNT + " but is " + getArgs().length + " instead!";
    }

    private void attachToKey(AudioClient audioClient) throws MarshallingException {
        checkArgumentNotNull(audioClient, "audio client");

        SocketChannel previousChannel = channelDataWriter.getChannel();
        channelDataWriter.setChannel(audioClient.getChannel());
        channelDataLoader.setChannel(audioClient.getChannel());
        channelDataWriter.saveData("attach " + clientResources.getLoggedClientEmail());

        String response = channelDataLoader.loadData();

        channelDataWriter.saveData(response);
        channelDataWriter.setChannel(previousChannel);
        channelDataLoader.setChannel(previousChannel);
    }
}

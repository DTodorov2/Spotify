package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelConnectionException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class PlaySongClientCommand extends ClientCommand {
    private static final int EXPECTED_INPUT_LENGTH = 1;

    public PlaySongClientCommand(String userInput, String[] arguments, ClientResources clientResources) {
        super(userInput, arguments, EXPECTED_INPUT_LENGTH, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        if (!clientResources.isClientLogged()) {
            return "The user must be logged in in order to play a song!";
        }

        if (clientResources.isStreaming()) {
            return "You must stop the current music in order to play a new one!";
        }

        try {
            channelDataWriter.saveData("check-song " + arguments[0]);
            String response = channelDataLoader.loadData();
            if (response.equals("yes")) {
                String songName = arguments[0];
                AudioClient audioClient = new AudioClient(songName, clientResources);
                attachToKey(audioClient);
                clientResources.setAudioClient(audioClient);
                clientResources.getExecutor().execute(audioClient);
                return "Song with name: " + songName + " will start playing shortly!";
            } else {
                return "There is no such song!";
            }
        } catch (ChannelConnectionException | SerializationDataException | DeserializationDataException e) {
            throw new ChannelCommunicationException("The server could not retrieve the song!", e);
        }

    }

    private void attachToKey(AudioClient audioClient) throws SerializationDataException, DeserializationDataException {
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

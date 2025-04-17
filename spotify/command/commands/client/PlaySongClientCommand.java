package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelConnectionException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.MarshallingException;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.MUSIC_STOP_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SONG_EXISTS;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.START_SONG_PLAYING;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.WRONG_COUNT_PARAMETERS;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class PlaySongClientCommand extends ClientCommand {

    private static final int EXPECTED_ARGS_COUNT = 2;
    private static final String ATTACH_COMMAND = "attach ";
    private static final String CHECK_SONG_COMMAND = "check-song ";

    public PlaySongClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        arguments = getArgs();
        if (isCorrectArgsCount(EXPECTED_ARGS_COUNT)) {
            if (clientResources.isStreaming()) {
                return MUSIC_STOP_REQUIRED;
            }

            try {
                String songName = arguments[1];
                String response = checkIfSongCanBePlayed(songName);
                return playTheSong(response, songName);
            } catch (ChannelConnectionException | MarshallingException e) {
                throw new ChannelCommunicationException("The server could not retrieve the song!", e);
            }
        }
        return WRONG_COUNT_PARAMETERS + "The count of the arguments is expected to be " +
                (EXPECTED_ARGS_COUNT - 1) + " but is " + (arguments.length - 1) + " instead!";
    }

    private String playTheSong(String response, String songName) throws ChannelConnectionException,
            MarshallingException {
        validateString(response, "server response");
        validateString(songName, "song name");

        if (response.equals(SONG_EXISTS)) {
            AudioClient audioClient = new AudioClient(songName, clientResources);
            attachToKey(audioClient);
            clientResources.setAudioClient(audioClient);
            clientResources.getExecutor().execute(audioClient);
            return START_SONG_PLAYING;
        } else {
            return response;
        }
    }

    private String checkIfSongCanBePlayed(String songName) throws MarshallingException {
        validateString(songName, "song name");

        channelDataWriter.saveData(CHECK_SONG_COMMAND + songName);
        return channelDataLoader.loadData();
    }

    private void attachToKey(AudioClient audioClient) throws MarshallingException {
        checkArgumentNotNull(audioClient, "audio client");

        SocketChannel previousChannel = channelDataWriter.getChannel();
        channelDataWriter.setChannel(audioClient.getChannel());
        channelDataLoader.setChannel(audioClient.getChannel());
        channelDataWriter.saveData(ATTACH_COMMAND + clientResources.getLoggedClientEmail());

        String response = channelDataLoader.loadData();

        channelDataWriter.saveData(response);
        channelDataWriter.setChannel(previousChannel);
        channelDataLoader.setChannel(previousChannel);
    }
}

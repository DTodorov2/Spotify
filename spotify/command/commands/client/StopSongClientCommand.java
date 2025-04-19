package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_STREAMING_SONG;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.STOP_SONG_PLAYING;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.getWrongParametersMessage;

public class StopSongClientCommand extends ClientCommand {

    private static final int EXPECTED_COUNT_ARGS = 1;

    public StopSongClientCommand(String userInput, ClientResources clientResources,
                                 ChannelDataWriter channelDataWriter, ChannelDataLoader channelDataLoader) {
        super(userInput, clientResources, channelDataWriter, channelDataLoader);
    }

    @Override
    public String execute() {
        arguments = getArgs();
        if (arguments != null && arguments.length != EXPECTED_COUNT_ARGS) {
            return getWrongParametersMessage(EXPECTED_COUNT_ARGS, arguments.length);
        }

        if (!clientResources.isClientLogged()) {
            return LOGIN_REQUIRED;
        }

        if (!clientResources.isStreaming()) {
            return NO_STREAMING_SONG;
        }

        AudioClient audioClient = clientResources.getAudioClient();
        audioClient.stopStreaming();

        return STOP_SONG_PLAYING;
    }

}

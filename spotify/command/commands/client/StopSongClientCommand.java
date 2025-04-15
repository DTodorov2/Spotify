package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_STREAMING_SONG;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.STOP_SONG_PLAYING;

public class StopSongClientCommand extends ClientCommand {

    public StopSongClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() {
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

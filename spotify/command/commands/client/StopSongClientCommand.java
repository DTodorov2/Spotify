package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;

public class StopSongClientCommand extends ClientCommand {

    public StopSongClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() {
        if (!clientResources.isClientLogged()) {
            return "The user must be logged in in order to stop the playing song!";
        }

        if (!clientResources.isStreaming()) {
            return "There is not a streaming song!";
        }

        AudioClient audioClient = clientResources.getAudioClient();
        stopStreaming(audioClient);

        return "The song has stopped playing!";
    }
}

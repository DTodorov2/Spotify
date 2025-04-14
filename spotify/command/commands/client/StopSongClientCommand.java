package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;

public class StopSongClientCommand extends ClientCommand {

    private static final int EXPECTED_INPUT_LENGTH = 0;

    public StopSongClientCommand(String userInput, String[] arguments, ClientResources clientResources) {
        super(userInput, arguments, EXPECTED_INPUT_LENGTH, clientResources);
    }

    @Override
    public String execute() {
        if (!clientResources.isClientLogged()) {
            return "The user must be logged in in order to stop the playing song!";
        }

        if (!clientResources.isStreaming()) {
            return "There is not a streaming song!";
        }

        if (clientResources.getAudioClient() != null) {

            clientResources.getAudioClient().setRunning(false);
            clientResources.setStreaming(false);
            clientResources.setAudioClient(null);

        }

        return "The song has stopped playing!";
    }
}

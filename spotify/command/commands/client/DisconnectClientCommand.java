package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_DISCONNECT;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.getWrongParametersMessage;

public class DisconnectClientCommand extends ClientCommand {

    private static final int EXPECTED_NUMBER_ARGS = 1;
    private static final String DISCONNECT_COMMAND = "disconnect";

    public DisconnectClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        try {
            channelDataWriter.saveData(DISCONNECT_COMMAND);
        } catch (SerializationDataException e) {
            throw new ChannelCommunicationException("Unable to communicate with the server!", e);
        }

        if (isCorrectArgsCount(EXPECTED_NUMBER_ARGS)) {
            clientResources.setClientLogged(false);
            if (clientResources.isStreaming()) {
                AudioClient audioClient =  clientResources.getAudioClient();
                if (audioClient != null) {
                    audioClient.stopStreaming();
                }
            }
            return SUCCESS_DISCONNECT;
        }

        return getWrongParametersMessage(EXPECTED_NUMBER_ARGS, getArgs().length);
    }

}

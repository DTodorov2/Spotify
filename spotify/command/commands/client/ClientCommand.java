package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public abstract class ClientCommand extends Command {

    private static final String ARGUMENTS_SEPARATOR = " ";

    protected final ClientResources clientResources;
    protected final ChannelDataLoader channelDataLoader;
    protected final ChannelDataWriter channelDataWriter;

    public ClientCommand(String userInput, ClientResources clientResources) {
        super(userInput);
        checkArgumentNotNull(clientResources, "client recourses object");

        this.clientResources = clientResources;
        validateString(userInput, "user input");
        this.userInput = userInput;
        channelDataLoader = new ChannelDataLoader(clientResources.getClientChannel());
        channelDataWriter = new ChannelDataWriter(clientResources.getClientChannel());
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        return manageClientCommand();
    }

    protected String manageClientCommand() throws ChannelCommunicationException {
        try {
            channelDataWriter.saveData(userInput);
            return channelDataLoader.loadData();
        } catch (SerializationDataException | DeserializationDataException e) {
            throw new ChannelCommunicationException("Unable to communicate with the channel!", e);
        }
    }

    public String[] getArgs() {
        return userInput.split(ARGUMENTS_SEPARATOR);
    }

    public boolean isCorrectArgsCount(int expectedArgsCount) {
        return getArgs().length == expectedArgsCount;
    }

//    public void stopStreaming(AudioClient audioClient) {
//        if (clientResources.isStreaming() && audioClient != null) {
//            audioClient.setRunning(false);
//            clientResources.setStreaming(false);
//            clientResources.setAudioClient(null);
//        }
//    }
}

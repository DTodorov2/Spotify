package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.MarshallingException;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public abstract class ClientCommand extends Command {

    private static final String ARGUMENTS_SEPARATOR = " ";

    protected final ClientResources clientResources;
    protected final ChannelDataLoader channelDataLoader;
    protected ChannelDataWriter channelDataWriter;

    public ClientCommand(String userInput,
                         ClientResources clientResources,
                         ChannelDataWriter channelDataWriter,
                         ChannelDataLoader channelDataLoader) {

        super(userInput);
        checkArgumentNotNull(clientResources, "client recourses object");

        this.clientResources = clientResources;
        validateString(userInput, "user input");
        this.userInput = userInput;

        checkArgumentNotNull(channelDataLoader, "channel data loader");
        checkArgumentNotNull(channelDataWriter, "channel data writer");
        this.channelDataLoader = channelDataLoader;
        this.channelDataWriter = channelDataWriter;

    }

    public void setChannelDataWriter(ChannelDataWriter channelDataWriter) {
        checkArgumentNotNull(channelDataWriter, "new channel data writer");
        this.channelDataWriter = channelDataWriter;
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        return manageClientCommand();
    }

    protected String manageClientCommand() throws ChannelCommunicationException {
        try {
            channelDataWriter.saveData(userInput);
            return channelDataLoader.loadData();
        } catch (MarshallingException e) {
            throw new ChannelCommunicationException("Unable to communicate with the channel!", e);
        }
    }

    public String[] getArgs() {
        return userInput.split(ARGUMENTS_SEPARATOR);
    }

    public boolean isCorrectArgsCount(int expectedArgsCount) {
        String[] args = getArgs();
        return args != null && args.length == expectedArgsCount;
    }

    public void setUserInput(String userInput) {
        checkArgumentNotNull(userInput, "user input");

        this.userInput = userInput;
    }

}

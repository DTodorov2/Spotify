package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

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

    protected final ClientResources clientResources;
    protected final ChannelDataLoader channelDataLoader;
    protected final ChannelDataWriter channelDataWriter;

    public ClientCommand(String userInput, String[] arguments, int expectedCountArguments,
                         ClientResources clientResources) {
        super(arguments, expectedCountArguments);
        checkArgumentNotNull(clientResources, "client recourses object");

        this.clientResources = clientResources;
        validateString(userInput, "use input");
        this.userInput = userInput;
        channelDataLoader = new ChannelDataLoader(clientResources.getClientChannel());
        channelDataWriter = new ChannelDataWriter(clientResources.getClientChannel());
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        return manageClientCommand(true);
    }

    protected String manageClientCommand(boolean mustBeLogged) throws ChannelCommunicationException {
        if (mustBeLogged && !clientResources.isClientLogged()) {
            return "The user must be logged in in order to use this operation!";
        }

        try {
            channelDataWriter.saveData(userInput);
            return channelDataLoader.loadData();
        } catch (SerializationDataException | DeserializationDataException e) {
            throw new ChannelCommunicationException("Unable to communicate with the channel!", e);
        }
    }
}

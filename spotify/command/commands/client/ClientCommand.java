package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationException;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public abstract class ClientCommand extends Command {

    protected final ClientResources clientResources;
    private final ChannelDataLoader channelDataLoader;
    private final ChannelDataWriter channelDataWriter;

    public ClientCommand(String userInput, String[] arguments, int expectedCountArguments,
                         ClientResources clientResources) {
        super(arguments, expectedCountArguments);
        checkArgumentNotNull(clientResources, "client recourses object");

        this.clientResources = clientResources;
        validateString(userInput, "use input");
        this.userInput = userInput;
        channelDataLoader = new ChannelDataLoader(clientResources);
        channelDataWriter = new ChannelDataWriter(clientResources);
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
            channelDataWriter.saveData(userInput, false);
            return channelDataLoader.loadData();
        } catch (SerializationException | DeserializationException e) {
            throw new ChannelCommunicationException("Unable to communicate with the channel!", e);
        }
    }
}

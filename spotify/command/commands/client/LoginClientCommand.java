package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public class LoginClientCommand extends ClientCommand {

    private static final int EXPECTED_NUMBER_ARGS = 3;

    public LoginClientCommand(String userInput,
                              ClientResources clientResources,
                              ChannelDataWriter channelDataWriter,
                              ChannelDataLoader channelDataLoader) {

        super(userInput, clientResources, channelDataWriter, channelDataLoader);

    }

    @Override
    public String execute() throws ChannelCommunicationException {
        String response = manageClientCommand();
        String[] args = getArgs();

        if (args.length == EXPECTED_NUMBER_ARGS) {
            clientResources.setLoggedClientEmail(args[1]);
            clientResources.setClientLogged(true);
        }

        return response;
    }

}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public class LoginClientCommand extends ClientCommand {

    private static final int EXPECTED_NUMBER_ARGS = 3;

    public LoginClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        String response = manageClientCommand(false);
        String[] args = getArgs();

        if (args.length == EXPECTED_NUMBER_ARGS) {
            clientResources.setLoggedClientEmail(args[1]);
            clientResources.setClientLogged(true);
        }

        return response;
    }

}

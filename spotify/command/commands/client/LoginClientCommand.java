package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public class LoginClientCommand extends ClientCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public LoginClientCommand(String userInput, String[] arguments, ClientResources clientResources) {
        super(userInput, arguments, EXPECTED_INPUT_LENGTH, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        String response = manageClientCommand(false);
        clientResources.setLoggedClientEmail(arguments[0]);
        clientResources.setClientLogged(true);
        return response;
    }

}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public class LogoutClientCommand extends ClientCommand {

    private static final int EXPECTED_INPUT_LENGTH = 0;

    public LogoutClientCommand(String userInput, String[] arguments, ClientResources clientResources) {
        super(userInput, arguments, EXPECTED_INPUT_LENGTH, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        //da proverq dali stream-va i ako da - da go spra
        String response = manageClientCommand(true);
        clientResources.setClientLogged(false);
        return response;
    }

}

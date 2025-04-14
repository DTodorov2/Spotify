package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public class DisconnectClientCommand extends ClientCommand {

    private static final int EXPECTED_INPUT_LENGTH = 0;

    public DisconnectClientCommand(String userInput, String[] arguments, ClientResources clientResources) {
        super(userInput, arguments, EXPECTED_INPUT_LENGTH, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        //tuka trqq proverqm, ako puska muzika da q spiram
        String response = super.execute();
        clientResources.setClientLogged(false);
        //setLogoutState()
        return response;

    }
}

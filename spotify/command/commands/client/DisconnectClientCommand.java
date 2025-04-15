package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public class DisconnectClientCommand extends ClientCommand {

    private static final int EXPECTED_NUMBER_ARGS = 1;

    public DisconnectClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        //tuka trqq proverqm, ako puska muzika da q spiram
        String response = super.execute();
        if (isCorrectArgsCount(EXPECTED_NUMBER_ARGS)) {
            clientResources.setClientLogged(false);
        }
        //setLogoutState()
        return response;

    }
}

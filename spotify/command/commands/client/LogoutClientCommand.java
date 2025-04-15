package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public class LogoutClientCommand extends ClientCommand {

    private static final int EXPECTED_NUMBER_ARGS = 1;

    public LogoutClientCommand(String userInput, ClientResources clientResources) {
        super(userInput, clientResources);
    }

    @Override
    public String execute() throws ChannelCommunicationException {
        //da proverq dali stream-va i ako da - da go spra
        String response = super.execute();

        if (isCorrectArgsCount(EXPECTED_NUMBER_ARGS)) {
            clientResources.setClientLogged(false);
        }

        return response;
    }

}

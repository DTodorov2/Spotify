package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

public class SearchCommand extends ServerCommand {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    public SearchCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() {
        boolean isUserLogged = isUserLogged();
        if (!isUserLogged) {
            return "You must be logged in in order to use the search option";
        }

        return serverResources.getSongsRepository().searchByWords(arguments).toString();
    }

}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public abstract class ServerCommand extends Command {

    protected final ServerResources serverResources;

    public ServerCommand(String[] arguments, int expectedCountArguments, ServerResources serverResources) {
        super(arguments, expectedCountArguments);
        checkArgumentNotNull(serverResources, "server recourses object");

        this.serverResources = serverResources;
    }

}

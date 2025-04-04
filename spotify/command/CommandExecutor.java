package bg.sofia.uni.fmi.mjt.spotify.command;

import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import java.util.Arrays;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class CommandExecutor {

    private static final String ARGUMENT_SEPARATOR = " ";

    private final CommandInvoker commandInvoker;

    public CommandExecutor(ServerResources serverResources) {
        checkArgumentNotNull(serverResources, "server resources");

        this.commandInvoker = new CommandInvoker(serverResources);
    }

    public String execute(String clientInput) {
        String[] tokens = clientInput.split(ARGUMENT_SEPARATOR);
        String command = tokens[0];
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        return commandInvoker.executeCommand(command, args);
    }

}

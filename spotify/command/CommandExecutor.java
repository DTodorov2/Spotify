package bg.sofia.uni.fmi.mjt.spotify.command;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.ServerCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import java.util.Arrays;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class CommandExecutor {

    private static final String ARGUMENT_SEPARATOR = " ";

    private final CommandInvoker commandInvoker;

    public CommandExecutor(ServerResources serverResources) {
        checkArgumentNotNull(serverResources, "server resources");

        this.commandInvoker = new ServerCommandInvoker(serverResources);
    }

    public CommandExecutor(ClientResources clientResources) {
        checkArgumentNotNull(clientResources, "client resources");

        this.commandInvoker = new ClientCommandInvoker(clientResources);
    }

    public String execute(String clientInput) throws ChannelCommunicationException {
        if (clientInput == null) {
            return "You must enter a command!";
        }

        String[] tokens = clientInput.split(ARGUMENT_SEPARATOR);
        String command = tokens[0];
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        if (command.equals("add-song-to")) {
            String[] songName = Arrays.copyOfRange(tokens, 2, tokens.length);
            args = new String[]{args[0], String.join("", songName)};
        }

        return commandInvoker.executeCommand(command, args);
    }

}

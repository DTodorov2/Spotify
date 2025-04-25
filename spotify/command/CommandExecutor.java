package bg.sofia.uni.fmi.mjt.spotify.command;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.ServerCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import java.util.Arrays;

import static bg.sofia.uni.fmi.mjt.spotify.command.CommandInvoker.ADD_SONG_TO;
import static bg.sofia.uni.fmi.mjt.spotify.command.CommandInvoker.PLAY;
import static bg.sofia.uni.fmi.mjt.spotify.command.CommandInvoker.REMOVE_SONG_FROM;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class CommandExecutor {

    private static final String ARGUMENT_SEPARATOR = " ";
    private static final String ARGS_JOIN_DELIMITER = "";

    private final CommandInvoker commandInvoker;

    public CommandExecutor(ServerResources serverResources, ServerCommandInvoker serverCommandInvoker) {
        checkArgumentNotNull(serverResources, "server resources");
        checkArgumentNotNull(serverCommandInvoker, "server command invoker");

        this.commandInvoker = serverCommandInvoker;
    }

    public CommandExecutor(ClientResources clientResources, ClientCommandInvoker clientCommandInvoker) {
        checkArgumentNotNull(clientResources, "client resources");
        checkArgumentNotNull(clientCommandInvoker, "client command invoker");

        this.commandInvoker = clientCommandInvoker;
    }

    public String execute(String clientInput) throws ChannelCommunicationException {
        if (clientInput == null) {
            return "You must enter a command!";
        }

        String[] tokens = clientInput.split(ARGUMENT_SEPARATOR);
        String command = tokens[0];
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        args = isArgsModificationNeeded(command, args, tokens);

        return commandInvoker.executeCommand(command, args);
    }

    private String[] isArgsModificationNeeded(String command, String[] args, String[] tokens) {
        boolean isSuccessfulSongModificationCommand = args.length > 1 &&
                (command.equals(REMOVE_SONG_FROM) || command.equals(ADD_SONG_TO));

        boolean isSuccessfulPlayCommand = args.length > 0 && command.equals(PLAY);

        int startIndex = isSuccessfulSongModificationCommand ? 2 : isSuccessfulPlayCommand ? 1 : 0;
        String[] songName = Arrays.copyOfRange(tokens, startIndex, tokens.length);

        return isSuccessfulSongModificationCommand ?
                new String[]{args[0], String.join(ARGS_JOIN_DELIMITER, songName)} :
                isSuccessfulPlayCommand ?
                        new String[]{String.join(ARGS_JOIN_DELIMITER, songName)} :
                        args;
    }

}

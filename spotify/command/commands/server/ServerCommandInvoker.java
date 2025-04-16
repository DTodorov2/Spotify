package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_COMMAND;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.WRONG_COUNT_PARAMETERS;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class ServerCommandInvoker implements CommandInvoker {

    private final Map<String, ServerCommandFactory> serverCommandMap;

    public ServerCommandInvoker(ServerResources serverResources) {
        checkArgumentNotNull(serverResources, "server resources");

        this.serverCommandMap = new HashMap<>();
        serverCommandMap.put(REGISTER, args -> new RegisterCommand(args, serverResources));
        serverCommandMap.put(LOGIN, args -> new LoginCommand(args, serverResources));
        serverCommandMap.put(SEARCH, args -> new SearchCommand(args, serverResources));
        serverCommandMap.put(TOP, args -> new TopSongsCommand(args, serverResources));
        serverCommandMap.put(CREATE_PLAYLIST, args -> new CreatePlaylistCommand(args, serverResources));
        serverCommandMap.put(REMOVE_PLAYLIST, args -> new RemovePlaylistCommand(args, serverResources));
        serverCommandMap.put(ADD_SONG_TO, args -> new AddSongToPlaylistCommand(args, serverResources));
        serverCommandMap.put(REMOVE_SONG_FROM, args -> new RemoveSongFromPlaylistCommand(args, serverResources));
        serverCommandMap.put(SHOW_PLAYLIST, args -> new ShowPlaylistCommand(args, serverResources));
        serverCommandMap.put(LOGOUT, args -> new LogoutCommand(args, serverResources));
        serverCommandMap.put(DISCONNECT, args -> new DisconnectCommand(args, serverResources));
        serverCommandMap.put(CHECK_SONG, args -> new IsThereSuchSongServerCommand(args, serverResources));
        serverCommandMap.put(PLAY, args -> new PlaySongServerCommand(args, serverResources));
        serverCommandMap.put(ATTACH, args -> new AttachEmailToKeyCommand(args, serverResources));
    }

    public String executeCommand(String command, String[] args) throws ChannelCommunicationException {
        validateString(command, "command");
        validateArrayOfString(args);

        try {
            ServerCommandFactory commandToExecute = serverCommandMap.get(command);
            if (commandToExecute == null) {
                return NO_SUCH_COMMAND;
            }

            Command newCommand = commandToExecute.create(args);
            return newCommand.execute();
        } catch (InvalidArgumentsCountException e) {
            return WRONG_COUNT_PARAMETERS + e.getMessage();
        }

    }

}

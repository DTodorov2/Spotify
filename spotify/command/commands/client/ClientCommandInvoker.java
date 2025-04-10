package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class ClientCommandInvoker implements CommandInvoker {
    private final Map<String, ClientCommandFactory> clientCommandMap;

    public ClientCommandInvoker(ClientResources clientResources) {

        checkArgumentNotNull(clientResources, "client resources");

        this.clientCommandMap = new HashMap<>();
        clientCommandMap.put(REGISTER,
                (input, args) -> new RegisterClientCommand(input, args, clientResources));
        clientCommandMap.put(DISCONNECT,
                (input, args) -> new DisconnectClientCommand(input, args, clientResources));
        clientCommandMap.put(LOGIN,
                (input, args) -> new LoginClientCommand(input, args, clientResources));
        clientCommandMap.put(SEARCH,
                (input, args) -> new SearchClientCommand(input, args, clientResources));
        clientCommandMap.put(TOP,
                (input, args) -> new RegisterClientCommand(input, args, clientResources));
        clientCommandMap.put(CREATE_PLAYLIST,
                (input, args) -> new CreatePlaylistClientCommand(input, args, clientResources));
        clientCommandMap.put(REMOVE_PLAYLIST,
                (input, args) -> new RemovePlaylistClientCommand(input, args, clientResources));
        clientCommandMap.put(ADD_SONG_TO,
                (input, args) -> new AddSongToPlaylistClientCommand(input, args, clientResources));
        clientCommandMap.put(ADD_SONG_ARTIST_TO,
                (input, args) -> new AddSongWithArtistToPlaylistClientCommand(input, args, clientResources));
        clientCommandMap.put(REMOVE_SONG_FROM,
                (input, args) -> new RemoveSongFromPlaylistClientCommand(input, args, clientResources));
        clientCommandMap.put(REMOVE_SONG_ARTIST,
                (input, args) -> new RemoveSongWithArtistFromPlaylistClientCommand(input, args, clientResources));
        clientCommandMap.put(SHOW_PLAYLIST,
                (input, args) -> new ShowPlaylistClientCommand(input, args, clientResources));
        clientCommandMap.put(LOGOUT,
                (input, args) -> new LogoutClientCommand(input, args, clientResources));
    }

    public String executeCommand(String command, String[] args) throws ChannelCommunicationException {
        validateString(command, "command");
        validateArrayOfString(args);

        String wholeInput = command + " " + String.join(" ", args);
        Command newCommand = clientCommandMap.get(command).create(wholeInput, args);
        return newCommand != null ? newCommand.execute() : "Unknown command!";
    }

}

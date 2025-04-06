package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.HashMap;
import java.util.Map;

public class ServerCommandInvoker implements CommandInvoker {

    private final Map<String, ServerCommandFactory> serverCommandMap;

    public ServerCommandInvoker(ServerResources serverResources) {

        this.serverCommandMap = new HashMap<>();
        serverCommandMap.put(REGISTER, args -> new RegisterCommand(args, serverResources));
        serverCommandMap.put(LOGIN, args -> new LoginCommand(args, serverResources));
        serverCommandMap.put(SEARCH, args -> new SearchCommand(args, serverResources));
        serverCommandMap.put(TOP, args -> new TopSongsCommand(args, serverResources));
        serverCommandMap.put(CREATE_PLAYLIST, args -> new CreatePlaylistCommand(args, serverResources));
        serverCommandMap.put(REMOVE_PLAYLIST, args -> new RemovePlaylistCommand(args, serverResources));
        serverCommandMap.put(ADD_SONG_TO, args -> new AddSongToPlaylistCommand(args, serverResources));
        serverCommandMap.put(ADD_SONG_ARTIST_TO, args -> new AddSongWithArtistToPlaylistCommand(args, serverResources));
        serverCommandMap.put(REMOVE_SONG_FROM, args -> new RemoveSongFromPlaylistCommand(args, serverResources));
        serverCommandMap.put(REMOVE_SONG_ARTIST, args -> new RemoveSongWithArtistFromPlaylistCommand(args, serverResources));
        serverCommandMap.put(SHOW_PLAYLIST, args -> new ShowPlaylistCommand(args, serverResources));
        serverCommandMap.put(LOGOUT, args -> new LogoutCommand(args, serverResources));
    }

    public String executeCommand(String command, String[] args) {
        Command newCommand = serverCommandMap.get(command).create(args);
        return newCommand != null ? newCommand.execute() : "Unknown command!";
    }

}

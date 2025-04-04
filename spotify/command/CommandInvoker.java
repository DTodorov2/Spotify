package bg.sofia.uni.fmi.mjt.spotify.command;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.LoginCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.RegisterCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.SearchCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {

    private final Map<String, CommandFactory> commandMap;

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String SEARCH = "search";
    private static final String DISCONNECT = "disconnect";
    private static final String TOP = "top";
    private static final String CREATE_PLAYLIST = "create-playlist";
    private static final String ADD_SONG_TO = "add-song-to";
    private static final String SHOW_PLAYLIST = "show-playlist";
    private static final String PLAY = "play";
    private static final String STOP = "stop";

    public CommandInvoker(ServerResources serverResources) {

        this.commandMap = new HashMap<>();
        commandMap.put(REGISTER, args -> new RegisterCommand(args, serverResources));
        commandMap.put(LOGIN, args -> new LoginCommand(args, serverResources));
        commandMap.put(SEARCH, args -> new SearchCommand(args, serverResources));
    }

    public String executeCommand(String command, String[] args) {
        Command newCommand = commandMap.get(command).create(args);
        return newCommand != null ? newCommand.execute() : "Unknown command!";
    }

}

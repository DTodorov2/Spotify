package bg.sofia.uni.fmi.mjt.spotify.command;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.SearchCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;

import java.nio.channels.SelectionKey;
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

    public CommandInvoker(UsersRepository usersRepository, SongsRepository songsRepository) {

        this.commandMap = new HashMap<>();
        commandMap.put(REGISTER, (args, key) -> new RegisterCommand(args, usersRepository, key));
        commandMap.put(LOGIN, (args, key) -> new LoginCommand(args, usersRepository, key));
        commandMap.put(SEARCH, (args, key) -> new SearchCommand(args, songsRepository, key, usersRepository));
    }

    public String executeCommand(String command, String[] args, SelectionKey key) {
        Command newCommand = commandMap.get(command).create(args, key);
        return newCommand != null ? newCommand.execute() : "Unknown command!";
    }

}

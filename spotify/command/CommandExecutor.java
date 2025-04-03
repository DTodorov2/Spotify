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

//    private static final String REGISTER = "register";
//    private static final String LOGIN = "login";
//    private static final String SEARCH = "search";
//    private static final String DISCONNECT = "disconnect";
//    private static final String TOP = "top";
//    private static final String CREATE_PLAYLIST = "create-playlist";
//    private static final String ADD_SONG_TO = "add-song-to";
//    private static final String SHOW_PLAYLIST = "show-playlist";
//    private static final String PLAY = "play";
//    private static final String STOP = "stop";
//
//    private final UsersRepository usersRepository;
//    private final SongsRepository songsRepository;

//    public CommandExecutor(UsersRepository usersRepository, SongsRepository songsRepository) {
//        checkArgumentNotNull(usersRepository, "user repository");
//        checkArgumentNotNull(songsRepository, "song repository");
//
//        this.usersRepository = usersRepository;
//        this.songsRepository = songsRepository;
//    }
//
//    public String execute(Command cmd) {
//
//    }

}

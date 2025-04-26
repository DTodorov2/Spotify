package bg.sofia.uni.fmi.mjt.spotify.command;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

public interface CommandInvoker {

    String REGISTER = "register";
    String LOGIN = "login";
    String SEARCH = "search";
    String DISCONNECT = "disconnect";
    String TOP = "top";
    String CREATE_PLAYLIST = "create-playlist";
    String REMOVE_PLAYLIST = "remove-playlist";
    String ADD_SONG_TO = "add-song-to";
    String REMOVE_SONG_FROM = "remove-song-from";
    String LOGOUT = "logout";
    String SHOW_PLAYLIST = "show-playlist";
    String PLAY = "play";
    String STOP = "stop";
    String CHECK_SONG = "check-song";
    String ATTACH = "attach";
    String SHOW_ALL_COMMANDS = "show-all-commands";

    String executeCommand(String command, String[] args) throws ChannelCommunicationException;

}

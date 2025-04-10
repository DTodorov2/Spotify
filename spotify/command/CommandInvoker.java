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
    String ADD_SONG_ARTIST_TO = "add-song-artist-to";
    String REMOVE_SONG_FROM = "remove-song-from";
    String REMOVE_SONG_ARTIST = "remove-song-artist-from";
    String LOGOUT = "logout";
    String SHOW_PLAYLIST = "show-playlist";
    String PLAY = "play";
    String STOP = "stop";

    String executeCommand(String command, String[] args) throws ChannelCommunicationException;

}

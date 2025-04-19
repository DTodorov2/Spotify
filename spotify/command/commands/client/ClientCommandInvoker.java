package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_COMMAND;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class ClientCommandInvoker implements CommandInvoker {

    private final Map<String, ClientCommandFactory> clientCommandMap;

    public ClientCommandInvoker(ClientResources clientResources) {
        checkArgumentNotNull(clientResources, "client resources");

        SocketChannel clientChannel = clientResources.getClientChannel();
        ChannelDataLoader channelDataLoader = new ChannelDataLoader(clientChannel);
        ChannelDataWriter channelDataWriter = new ChannelDataWriter(clientChannel);

        this.clientCommandMap = new HashMap<>();
        clientCommandMap.put(REGISTER, input ->
                        new RegisterClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(DISCONNECT, input ->
                        new DisconnectClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(LOGIN, input ->
                        new LoginClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(SEARCH, input ->
                        new SearchClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(TOP, input ->
                        new TopSongsClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(CREATE_PLAYLIST, input ->
                        new CreatePlaylistClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(REMOVE_PLAYLIST, input ->
                        new RemovePlaylistClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(ADD_SONG_TO, input ->
                        new AddSongToPlaylistClientCommand(input, clientResources,
                                channelDataWriter, channelDataLoader));
        clientCommandMap.put(REMOVE_SONG_FROM, input ->
                        new RemoveSongFromPlaylistClientCommand(input, clientResources,
                                channelDataWriter, channelDataLoader));
        clientCommandMap.put(SHOW_PLAYLIST, input ->
                        new ShowPlaylistClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(LOGOUT, input ->
                        new LogoutClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(CHECK_SONG, input ->
                        new IsThereSuchSongClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(PLAY, input ->
                        new PlaySongClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(STOP, input ->
                        new StopSongClientCommand(input, clientResources, channelDataWriter, channelDataLoader));
        clientCommandMap.put(SHOW_ALL_COMMANDS, input ->
                        new ShowAllCommandsClientCommand(input, clientResources, channelDataWriter, channelDataLoader));

    }

    public String executeCommand(String command, String[] args)
            throws ChannelCommunicationException {
        validateString(command, "command");
        validateArrayOfString(args);

        String wholeInput = command + " " + String.join(" ", args);

        ClientCommandFactory commandToExecute = clientCommandMap.get(command);
        if (commandToExecute == null) {
            return NO_SUCH_COMMAND;
        }

        ClientCommand newCommand = (ClientCommand) commandToExecute.create(wholeInput);
        return newCommand.execute();
    }

}

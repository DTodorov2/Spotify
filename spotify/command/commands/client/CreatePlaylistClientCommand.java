package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;

public class CreatePlaylistClientCommand extends ClientCommand {

    public CreatePlaylistClientCommand(String userInput,
                                       ClientResources clientResources,
                                       ChannelDataWriter channelDataWriter,
                                       ChannelDataLoader channelDataLoader) {

        super(userInput, clientResources, channelDataWriter, channelDataLoader);

    }

}

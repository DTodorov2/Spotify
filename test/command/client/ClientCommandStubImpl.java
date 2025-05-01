package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;

class ClientCommandStubImpl extends ClientCommand {

    public ClientCommandStubImpl(String userInput,
                                 ClientResources clientResources,
                                 ChannelDataLoader mockLoader,
                                 ChannelDataWriter mockWriter) {
        super(userInput, clientResources, mockWriter, mockLoader);
    }

}


package bg.sofia.uni.fmi.mjt.spotify.command.commands.client;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;

public interface ClientCommandFactory {

    Command create(String userInput);

}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;

public interface ServerCommandFactory {
    Command create(String[] args) throws InvalidArgumentsCountException;
}

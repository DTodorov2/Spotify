package bg.sofia.uni.fmi.mjt.spotify.command;

//това е функционален интерфейс, който ни дава възможността в мапа с команди да слагаме ламнбда функции

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.nio.channels.SelectionKey;

public interface CommandFactory {
    Command create(String[] args);
}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

//това е функционален интерфейс, който ни дава възможността в мапа с команди да слагаме ламнбда функции
import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;

public interface ServerCommandFactory {
    Command create(String[] args);
}

package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

//това е функционален интерфейс, който ни дава възможността в мапа с команди да слагаме ламнбда функции
import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;

public interface ServerCommandFactory {
    Command create(String[] args) throws InvalidArgumentsCountException;
}

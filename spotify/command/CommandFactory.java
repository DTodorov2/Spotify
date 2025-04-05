package bg.sofia.uni.fmi.mjt.spotify.command;

//това е функционален интерфейс, който ни дава възможността в мапа с команди да слагаме ламнбда функции
import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;

public interface CommandFactory {
    Command create(String[] args);
}

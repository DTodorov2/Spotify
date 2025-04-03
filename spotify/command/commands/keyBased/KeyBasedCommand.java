package bg.sofia.uni.fmi.mjt.spotify.command.commands.keyBased;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public abstract class KeyBasedCommand extends Command {

    protected SelectionKey key;

    public KeyBasedCommand(String[] arguments, int expectedCountArguments, SelectionKey key, UsersRepository usersRepository) {
        super(arguments, expectedCountArguments, usersRepository);
        checkArgumentNotNull(key, "selection key");

        this.key = key;
    }

    public boolean isUserLogged() {
        //trqq li mi proverka dali key e null
        String userId = (String) key.attachment();
        return userId != null;

    }
}

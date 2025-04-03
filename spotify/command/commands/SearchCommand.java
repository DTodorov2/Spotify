package bg.sofia.uni.fmi.mjt.spotify.command.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class SearchCommand extends Command {

    private static final int EXPECTED_INPUT_LENGTH = 2;

    private final SongsRepository songsRepository;

    public SearchCommand(String[] arguments, SongsRepository songsRepository,
                         SelectionKey key, UsersRepository usersRepository) {
        super(arguments, EXPECTED_INPUT_LENGTH, key, usersRepository);
        checkArgumentNotNull(songsRepository, "song repository");

        this.songsRepository = songsRepository;
    }

    @Override
    public String execute() {
        boolean isUserLogged = isUserLogged();
        if (!isUserLogged) {
            return "You must be logged in in order to use the search option";
        }

        return songsRepository.searchByWords(arguments).toString();
    }

}

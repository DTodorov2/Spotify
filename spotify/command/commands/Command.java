package bg.sofia.uni.fmi.mjt.spotify.command.commands;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;

import java.util.Arrays;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;

public abstract class Command {

    private static final String ARGUMENT_SEPARATOR = " ";

    protected final String[] arguments;
    protected String userInput = null;

    public Command(String[] arguments, int expectedCountArguments) {
        System.out.println("arguments: " + Arrays.toString(arguments));
        validateArrayOfString(arguments);
        validateInt(expectedCountArguments, "count of the expected arguments");
        validateArgCount(arguments, expectedCountArguments);

        this.arguments = arguments;

    }

    public String[] getArguments() {
        return arguments;
    }

    public String displayListOfSongs(List<Song> songList) {
        checkArgumentNotNull(songList, "list of songs");

        StringBuilder sb = new StringBuilder();
        sb.append("Songs: ")
                .append(System.lineSeparator());
        int index = 1;
        for (Song song : songList) {
            sb.append(index++)
                    .append(". ")
                    .append(song.toString());
        }

        return sb.toString();
    }

    public abstract String execute() throws ChannelCommunicationException;
}

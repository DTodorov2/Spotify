package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.ShowPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.EMPTY_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_PLAYLIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShowPlaylistCommandTest {

    private static final String PLAYLIST_NAME = "playlist-name";

    private ShowPlaylistCommand command;
    private ServerResources mockResources;
    private PlaylistsRepository mockRepo;
    private Playlist mockPlaylist;

    @BeforeEach
    void setUp() {
        mockResources = mock(ServerResources.class);
        User mockUser = mock(User.class);
        mockRepo = mock(PlaylistsRepository.class);

        when(mockResources.getLoggedUser()).thenReturn(mockUser);
        when(mockUser.getPlaylistsRepository()).thenReturn(mockRepo);
        mockPlaylist = mock(Playlist.class);
        when(mockRepo.getById(PLAYLIST_NAME)).thenReturn(mockPlaylist);

        command = new ShowPlaylistCommand(new String[]{PLAYLIST_NAME}, mockResources);
    }

    @Test
    public void testExecuteWithInvalidArgsCount() {
        command.setArguments(new String[]{});

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    public void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new ShowPlaylistCommand(null, mockResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new ShowPlaylistCommand(arguments, mockResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String[] arguments = {"parameter"};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new ShowPlaylistCommand(arguments, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new ShowPlaylistCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteWhenUserNotLoggedIn() {
        when(mockResources.getLoggedUser()).thenReturn(null);

        String result = executeCommand();

        assertEquals(LOGIN_REQUIRED, result);
    }

    @Test
    void testExecuteWhenPlaylistDoesNotExist() {
        when(mockRepo.getById(PLAYLIST_NAME)).thenReturn(null);

        String result = executeCommand();

        assertEquals(NO_SUCH_PLAYLIST, result);
    }

    @Test
    void testExecuteWhenPlaylistIsEmpty() {
        when(mockPlaylist.getAllSongs()).thenReturn(Collections.emptyMap());

        String result = executeCommand();

        assertEquals(EMPTY_PLAYLIST, result);
    }

    @Test
    void testExecuteWithValidPlaylistWithSongs() {
        Song song = new Song("Imagine", "John Lennon", 123);
        Map<String, Song> songs = Map.of("Imagine", song);

        when(mockPlaylist.getAllSongs()).thenReturn(songs);

        String result = executeCommand();

        String expected = "Songs: " + System.lineSeparator() +
                "1. " + song;

        assertEquals(expected, result);
    }

    private String executeCommand() {
        String result = null;
        try {
            result = command.execute();
            return result;
        } catch (InvalidArgumentsCountException e) {
            fail("The create-playlist command could not execute!");
        }

        return result;
    }

}

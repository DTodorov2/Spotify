package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.RemoveSongFromPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_SONG;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SONG_NOT_IN_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_SONG_REMOVAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RemoveSongFromPlaylistCommandTest {

    private static final String PLAYLIST_NAME = "playlist";
    private static final String SONG_NAME = "song";
    private static final String[] ARGUMENTS = new String[]{PLAYLIST_NAME, SONG_NAME};

    private static ServerResources mockResources;
    private static SongsRepository mockSongsRepository;
    private static PlaylistsRepository mockPlaylistsRepository;
    private static Playlist mockPlaylist;
    private static Song mockSong;

    private RemoveSongFromPlaylistCommand command;

    @BeforeAll
    static void setUpBeforeAll() {
        mockResources = mock(ServerResources.class);
        mockSongsRepository = mock(SongsRepository.class);
        mockPlaylistsRepository = mock(PlaylistsRepository.class);
        mockPlaylist = mock(Playlist.class);
        mockSong = mock(Song.class);
    }

    @BeforeEach
    void setUp() {
        User mockUser = mock(User.class);

        when(mockResources.getLoggedUser()).thenReturn(mockUser);
        when(mockResources.getSongsRepository()).thenReturn(mockSongsRepository);
        when(mockUser.getPlaylistsRepository()).thenReturn(mockPlaylistsRepository);
        when(mockPlaylistsRepository.getById(PLAYLIST_NAME)).thenReturn(mockPlaylist);
        when(mockSongsRepository.getSongByName(SONG_NAME)).thenReturn(mockSong);

        command = new RemoveSongFromPlaylistCommand(ARGUMENTS, mockResources);
    }

    @Test
    void testExecuteWithInvalidArgsCount() {
        command.setArguments(new String[]{});

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemoveSongFromPlaylistCommand(null, mockResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemoveSongFromPlaylistCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArg() {
        String[] arguments = {"parameter"};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemoveSongFromPlaylistCommand(arguments, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemoveSongFromPlaylistCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteNotLoggedUser() {
        when(mockResources.getLoggedUser()).thenReturn(null);

        String result = executeCommand();
        assertEquals(LOGIN_REQUIRED, result, LOGIN_REQUIRED + " message is expected to be shown!");
    }

    @Test
    void testExecuteWithNoSuchPlaylist() {
        when(mockPlaylistsRepository.getById(PLAYLIST_NAME)).thenReturn(null);

        String result = executeCommand();
        assertEquals(NO_SUCH_PLAYLIST, result, NO_SUCH_PLAYLIST + " message is expected to be shown!");
    }

    @Test
    void testExecuteWhenSongDoesNotExist() {
        when(mockSongsRepository.getSongByName(SONG_NAME)).thenReturn(null);

        String result = executeCommand();
        assertEquals(NO_SUCH_SONG, result, NO_SUCH_SONG + " message is expected to be shown!");
    }

    @Test
    void testExecuteWithSongNotInPlaylist() {
        when(mockPlaylist.removeSong(mockSong)).thenReturn(false);

        String result = executeCommand();
        assertEquals(SONG_NOT_IN_PLAYLIST, result, SONG_NOT_IN_PLAYLIST + " message is expected to be shown!");
    }

    @Test
    void testExecuteSuccessfulRemoval() {
        when(mockPlaylist.removeSong(mockSong)).thenReturn(true);

        String result = executeCommand();
        assertEquals(SUCCESS_SONG_REMOVAL, result, SUCCESS_SONG_REMOVAL + " message is expected to be shown!");
    }

    private String executeCommand() {
        String result = null;
        try {
            result = command.execute();
            return result;
        } catch (InvalidArgumentsCountException e) {
            fail("The remove command could not execute!");
        }

        return result;
    }

}

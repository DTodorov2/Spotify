package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.RemoveSongFromPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
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

    private ServerResources mockResources;
    private SongsRepository mockSongsRepository;
    private PlaylistsRepository mockPlaylistsRepository;
    private Playlist mockPlaylist;
    private Song mockSong;

    private RemoveSongFromPlaylistCommand command;

    @BeforeEach
    void setUp() {
        mockResources = mock(ServerResources.class);
        User mockUser = mock(User.class);
        mockSongsRepository = mock(SongsRepository.class);
        mockPlaylistsRepository = mock(PlaylistsRepository.class);
        mockPlaylist = mock(Playlist.class);
        mockSong = mock(Song.class);

        when(mockResources.getLoggedUser()).thenReturn(mockUser);
        when(mockResources.getSongsRepository()).thenReturn(mockSongsRepository);
        when(mockUser.getPlaylistsRepository()).thenReturn(mockPlaylistsRepository);
        when(mockPlaylistsRepository.getById(PLAYLIST_NAME)).thenReturn(mockPlaylist);
        when(mockSongsRepository.getSongByName(SONG_NAME)).thenReturn(mockSong);

        command = new RemoveSongFromPlaylistCommand(ARGUMENTS, mockResources);
    }

    @Test
    public void testExecuteWithInvalidArgsCount() {
        String[] arguments = {};
        command = new RemoveSongFromPlaylistCommand(arguments, mockResources);

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    public void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemoveSongFromPlaylistCommand(null, mockResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemoveSongFromPlaylistCommand(arguments, mockResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String[] arguments = {"parameter"};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemoveSongFromPlaylistCommand(arguments, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemoveSongFromPlaylistCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteReturnsLoginRequiredWhenNotLoggedIn() {
        when(mockResources.getLoggedUser()).thenReturn(null);

        String result = executeCommand();

        assertEquals(LOGIN_REQUIRED, result);
    }

    @Test
    void testExecuteReturnsNoSuchPlaylistWhenMissing() {
        when(mockPlaylistsRepository.getById(PLAYLIST_NAME)).thenReturn(null);

        String result = executeCommand();

        assertEquals(NO_SUCH_PLAYLIST, result);
    }

    @Test
    void testExecuteReturnsNoSuchSongWhenSongNotFound() {
        when(mockSongsRepository.getSongByName(SONG_NAME)).thenReturn(null);

        String result = executeCommand();

        assertEquals(NO_SUCH_SONG, result);
    }

    @Test
    void testExecuteReturnsSongNotInPlaylistWhenRemoveFails() {
        when(mockPlaylist.removeSong(mockSong)).thenReturn(false);

        String result = executeCommand();

        assertEquals(SONG_NOT_IN_PLAYLIST, result);
    }

    @Test
    void testExecuteReturnsSuccessWhenSongRemoved() {
        when(mockPlaylist.removeSong(mockSong)).thenReturn(true);
        String result = executeCommand();

        assertEquals(SUCCESS_SONG_REMOVAL, result);
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

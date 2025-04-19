package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.AddSongToPlaylistCommand;
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
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SONG_ALREADY_IN_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_ADD_SONG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddSongToPlaylistCommandTest {

    private ServerResources mockServerResources;
    private User mockUser;
    private Playlist mockPlaylist;
    private Song mockSong;
    private AddSongToPlaylistCommand command;
    private SongsRepository mockSongsRepo;
    private final String[] arguments = {"playlistName", "songName"};

    @BeforeEach
    public void setUp() {
        mockServerResources = mock(ServerResources.class);
        mockUser = mock(User.class);
        mockPlaylist = mock(Playlist.class);
        mockSongsRepo = mock(SongsRepository.class);
        mockSong = mock(Song.class);
    }

    @Test
    public void testExecuteWithInvalidArgsCount() {
        String[] arguments = {"parameter"};
        command = new AddSongToPlaylistCommand(arguments, mockServerResources);

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    public void testExecuteWithNullArgs() {

        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new AddSongToPlaylistCommand(null, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new AddSongToPlaylistCommand(arguments, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String[] arguments = {"parameter"};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new AddSongToPlaylistCommand(arguments, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new AddSongToPlaylistCommand(arguments, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWhenUserNotLoggedIn() {
        when(mockServerResources.getLoggedUser()).thenReturn(null);

        command = new AddSongToPlaylistCommand(arguments, mockServerResources);
        String result = executeCommand();

        assertEquals(LOGIN_REQUIRED, result, "Login is required in order to use the add-song-to command!");
    }

    @Test
    public void testExecuteWhenPlaylistNotFound() {

        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockUser.getPlaylistsRepository()).thenReturn(mock(PlaylistsRepository.class));
        when(mockUser.getPlaylistsRepository().getById(anyString())).thenReturn(null);

        command = new AddSongToPlaylistCommand(arguments, mockServerResources);
        String result = executeCommand();

        assertEquals(NO_SUCH_PLAYLIST, result, "Expected " + NO_SUCH_PLAYLIST + " message to be shown!");
    }

    @Test
    public void testExecuteWhenSongNotFound() {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockUser.getPlaylistsRepository()).thenReturn(mock(PlaylistsRepository.class));
        when(mockUser.getPlaylistsRepository().getById(anyString())).thenReturn(mockPlaylist);
        when(mockServerResources.getSongsRepository()).thenReturn(mockSongsRepo);
        when(mockSongsRepo.getSongByName(anyString())).thenReturn(null);

        command = new AddSongToPlaylistCommand(arguments, mockServerResources);
        String result = executeCommand();

        assertEquals(NO_SUCH_SONG, result, "Expected " + NO_SUCH_SONG + " message to be shown!");
    }

    @Test
    public void testExecuteWhenSongAlreadyInPlaylist() {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockUser.getPlaylistsRepository()).thenReturn(mock(PlaylistsRepository.class));
        when(mockUser.getPlaylistsRepository().getById(anyString())).thenReturn(mockPlaylist);
        when(mockServerResources.getSongsRepository()).thenReturn(mockSongsRepo);
        when(mockSongsRepo.getSongByName(anyString())).thenReturn(mockSong);
        when(mockPlaylist.addSong(mockSong)).thenReturn(false);

        command = new AddSongToPlaylistCommand(arguments, mockServerResources);
        String result = executeCommand();

        String errMess = "Expected " + SONG_ALREADY_IN_PLAYLIST + " message to be shown!";
        assertEquals(SONG_ALREADY_IN_PLAYLIST, result, errMess);
    }

    @Test
    public void testExecuteSuccessfully() {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockUser.getPlaylistsRepository()).thenReturn(mock(PlaylistsRepository.class));
        when(mockUser.getPlaylistsRepository().getById(anyString())).thenReturn(mockPlaylist);
        when(mockServerResources.getSongsRepository()).thenReturn(mockSongsRepo);
        when(mockSongsRepo.getSongByName(anyString())).thenReturn(mockSong);
        when(mockPlaylist.addSong(mockSong)).thenReturn(true);

        command = new AddSongToPlaylistCommand(arguments, mockServerResources);
        String result = executeCommand();

        String errMess = "Expected " + SUCCESS_ADD_SONG + " message to be shown!";
        assertEquals(SUCCESS_ADD_SONG, result, errMess);
    }

    private String executeCommand() {
        String result = null;
        try {
            result = command.execute();
            return result;
        } catch (InvalidArgumentsCountException e) {
            fail("The add-song-to command could not execute!");
        }

        return result;
    }
}

package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.PlaySongServerCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.EMPTY_MESSAGE;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_SONG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaySongServerCommandTest {

    private static final String EXAMPLE_PARAMETER = "parameter";
    private static final String EXAMPLE_SONG_TITLE = "song";

    private static ServerResources mockServerResources;
    private static User mockUser;
    private static SongsRepository mockSongsRepo;
    private static UsersRepository mockUsersRepo;
    private static Song mockSong;
    private static ExecutorService mockExecutor;
    private static SelectionKey mockSelectionKey;
    private PlaySongServerCommand command;

    @BeforeAll
    static void setUpBeforeAll() {
        mockServerResources = mock(ServerResources.class);
        mockUser = mock(User.class);
        mockSongsRepo = mock(SongsRepository.class);
        mockUsersRepo = mock(UsersRepository.class);
        mockSong = mock(Song.class);
        mockExecutor = mock(ExecutorService.class);
        mockSelectionKey = mock(SelectionKey.class);
    }

    @BeforeEach
    void setUpBeforeEach() {
        SocketChannel mockSocketChannel = mock(SocketChannel.class);

        when(mockServerResources.getSongsRepository()).thenReturn(mockSongsRepo);
        when(mockServerResources.getUsersRepository()).thenReturn(mockUsersRepo);
        when(mockServerResources.getExecutor()).thenReturn(mockExecutor);
        when(mockServerResources.getSelectionKey()).thenReturn(mockSelectionKey);
        when(mockSelectionKey.channel()).thenReturn(mockSocketChannel);
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);

        command = new PlaySongServerCommand(new String[]{EXAMPLE_SONG_TITLE}, mockServerResources);
    }

    @Test
    public void testExecuteWithInvalidArgsCount() {
        command.setArguments(new String[]{EXAMPLE_PARAMETER, EXAMPLE_PARAMETER});
        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    public void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new PlaySongServerCommand(null, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {EXAMPLE_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new PlaySongServerCommand(arguments, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new PlaySongServerCommand(new String[]{EXAMPLE_PARAMETER}, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {EXAMPLE_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new PlaySongServerCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testExecuteWhenNotLoggedIn() {
        when(mockServerResources.getLoggedUser()).thenReturn(null);

        String result = executeCommand();
        assertEquals(LOGIN_REQUIRED, result, LOGIN_REQUIRED + " message is expected to be shown!");
    }

    @Test
    void testExecuteWhenSongDoesNotExist() {
        when(mockSongsRepo.getSongByName(EXAMPLE_SONG_TITLE)).thenReturn(null);

        String result = executeCommand();
        assertEquals(NO_SUCH_SONG, result, NO_SUCH_SONG + " message is expected to be shown!");
    }

    @Test
    void testExecuteSuccess() {
        when(mockSongsRepo.getSongByName(EXAMPLE_SONG_TITLE)).thenReturn(mockSong);

        Playlist mockPlaylist = mock(Playlist.class);
        when(mockPlaylist.contains(EXAMPLE_SONG_TITLE)).thenReturn(true);

        PlaylistsRepository mockPlaylistsRepo = mock(PlaylistsRepository.class);
        when(mockPlaylistsRepo.getMap()).thenReturn(Map.of("playlist1", mockPlaylist));
        when(mockUser.getPlaylistsRepository()).thenReturn(mockPlaylistsRepo);

        User otherUser = mock(User.class);
        when(otherUser.getPlaylistsRepository()).thenReturn(mockPlaylistsRepo);
        when(mockUsersRepo.getMap()).thenReturn(Map.of("user1", mockUser, "user2", otherUser));

        String result = executeCommand();

        assertEquals(EMPTY_MESSAGE, result, "Successful execution of the command is expected!");
        verify(mockSong).incrementPlayCount();
        verify(mockPlaylist, times(2)).incrementPlayingsCountOf(EXAMPLE_SONG_TITLE);
        verify(mockExecutor).execute(any(AudioServer.class));
        verify(mockSelectionKey).cancel();
    }

    @Test
    void testExecuteWithInvalidArgumentsCount() {
        command.setArguments(new String[]{EXAMPLE_PARAMETER, EXAMPLE_PARAMETER});

        String mess = "InvalidArgumentsCountException is expected to be thrown when invalid argument count is passed";
        assertThrows(InvalidArgumentsCountException.class, command::execute, mess);
    }

    private String executeCommand() {
        String result = null;
        try {
            result = command.execute();
            return result;
        } catch (ChannelCommunicationException | InvalidArgumentsCountException e) {
            fail("The logout command could not execute!");
        }

        return result;
    }

}

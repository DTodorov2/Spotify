package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.PlaySongServerCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaySongClientCommandTest {

    private static final String SONG_NAME = "TestSong";

    private ServerResources mockServerResources;
    private SongsRepository mockSongsRepository;
    private UsersRepository mockUsersRepository;
    private ExecutorService mockExecutor;
    private SelectionKey mockSelectionKey;

    private User mockUser;
    private Song mockSong;
    private Playlist mockPlaylist;
    private PlaylistsRepository mockPlaylistRepo;

    @BeforeEach
    void setUp() {
        mockServerResources = mock(ServerResources.class);
        mockSongsRepository = mock(SongsRepository.class);
        mockUsersRepository = mock(UsersRepository.class);
        mockExecutor = mock(ExecutorService.class);
        mockSelectionKey = mock(SelectionKey.class);
        SocketChannel mockSocketChannel = mock(SocketChannel.class);

        mockUser = mock(User.class);
        mockSong = mock(Song.class);
        mockPlaylist = mock(Playlist.class);
        mockPlaylistRepo = mock(PlaylistsRepository.class);

        when(mockServerResources.getSongsRepository()).thenReturn(mockSongsRepository);
        when(mockServerResources.getUsersRepository()).thenReturn(mockUsersRepository);
        when(mockServerResources.getExecutor()).thenReturn(mockExecutor);
        when(mockServerResources.getSelectionKey()).thenReturn(mockSelectionKey);
        when(mockSelectionKey.channel()).thenReturn(mockSocketChannel);
    }

    @Test
    void testExecuteReturnsLoginRequiredIfNoLoggedUser() throws Exception {
        when(mockServerResources.getLoggedUser()).thenReturn(null);

        PlaySongServerCommand command = new PlaySongServerCommand(new String[]{SONG_NAME}, mockServerResources);
        String result = command.execute();

        assertEquals(LOGIN_REQUIRED, result);
    }

    @Test
    void testExecuteReturnsNoSuchSongIfSongNotFound() throws Exception {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockSongsRepository.getSongByName(SONG_NAME)).thenReturn(null);

        PlaySongServerCommand command = new PlaySongServerCommand(new String[]{SONG_NAME}, mockServerResources);
        String result = command.execute();

        assertEquals(CommandMessages.NO_SUCH_SONG, result);
    }

    @Test
    void testExecuteStartsAudioServerAndIncrementsPlayCount() throws Exception {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockSongsRepository.getSongByName(SONG_NAME)).thenReturn(mockSong);
        when(mockUser.getPlaylistsRepository()).thenReturn(mockPlaylistRepo);

        when(mockPlaylistRepo.getMap()).thenReturn(Map.of("playlist1", mockPlaylist));
        when(mockUsersRepository.getMap()).thenReturn(Map.of("user1", mockUser));
        when(mockPlaylist.contains(SONG_NAME)).thenReturn(true);

        PlaySongServerCommand command = new PlaySongServerCommand(new String[]{SONG_NAME}, mockServerResources);
        String result = command.execute();

        assertEquals(CommandMessages.EMPTY_MESSAGE, result);

        verify(mockSong).incrementPlayCount();
        verify(mockPlaylist).incrementPlayingsCountOf(SONG_NAME);
        verify(mockExecutor).execute(any(AudioServer.class));
        verify(mockSelectionKey).cancel();
    }

    @Test
    void testExecuteDoesNotIncrementPlaylistIfSongNotInIt() throws Exception {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockSongsRepository.getSongByName(SONG_NAME)).thenReturn(mockSong);
        when(mockUser.getPlaylistsRepository()).thenReturn(mockPlaylistRepo);

        when(mockPlaylistRepo.getMap()).thenReturn(Map.of("playlist1", mockPlaylist));
        when(mockUsersRepository.getMap()).thenReturn(Map.of("user1", mockUser));
        when(mockPlaylist.contains(SONG_NAME)).thenReturn(false);

        PlaySongServerCommand command = new PlaySongServerCommand(new String[]{SONG_NAME}, mockServerResources);
        command.execute();

        verify(mockPlaylist, never()).incrementPlayingsCountOf(any());
    }

}

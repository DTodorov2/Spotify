package bg.sofia.uni.fmi.mjt.server.model;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        PlaylistsRepository playlistsRepository = new PlaylistsRepository();

        user = new User("test@example.com", "pass", playlistsRepository);
    }

    @Test
    public void testAddPlaylistWithUniquePlaylist() {
        String playlistName = "playlistName";
        boolean result = user.addPlaylist(playlistName);

        assertTrue(result, "The playlist must be added but is not!");
    }

    @Test
    public void testAddPlaylistWithExistingPlaylist() {
        String playlistName = "playlistName";
        user.addPlaylist(playlistName);
        boolean result = user.addPlaylist(playlistName);

        assertFalse(result, "The playlist must not be added but it is!");
    }

    @Test
    public void testAddPlaylistWithNullPlaylistName() {
        assertThrows(IllegalArgumentException.class, () -> user.addPlaylist(null),
                "IllegalArgumentException is expected to be thrown when playlist name is null!");
    }

    @Test
    public void testAddPlaylistWithBlankPlaylistName() {
        assertThrows(IllegalArgumentException.class, () -> user.addPlaylist("   "),
                "IllegalArgumentException is expected to be thrown when playlist name is blank!");
    }

    @Test
    public void testAddPlaylistWithEmptyPlaylistName() {
        assertThrows(IllegalArgumentException.class, () -> user.addPlaylist(""),
                "IllegalArgumentException is expected to be thrown when playlist name is empty!");
    }

    @Test
    public void testRemovePlaylistWithExistingName() {
        String playlistName = "playlistName";
        user.addPlaylist(playlistName);
        boolean result = user.removePlaylist(playlistName);

        assertTrue(result, "The playlist must be removed but is not!");
    }

    @Test
    public void testRemovePlaylistWithNotExistingName() {
        String playlistName = "playlistName";
        boolean result = user.removePlaylist(playlistName);

        assertFalse(result, "The playlist must not be removed but it is!");
    }

    @Test
    public void testRemovePlaylistWithNullPlaylistName() {
        assertThrows(IllegalArgumentException.class, () -> user.removePlaylist(null),
                "IllegalArgumentException is expected to be thrown when playlist name is null!");
    }

    @Test
    public void testRemovePlaylistWithBlankPlaylistName() {
        assertThrows(IllegalArgumentException.class, () -> user.removePlaylist("   "),
                "IllegalArgumentException is expected to be thrown when playlist name is blank!");
    }

    @Test
    public void testRemovePlaylistWithEmptyPlaylistName() {
        assertThrows(IllegalArgumentException.class, () -> user.removePlaylist(""),
                "IllegalArgumentException is expected to be thrown when playlist name is empty!");
    }

}

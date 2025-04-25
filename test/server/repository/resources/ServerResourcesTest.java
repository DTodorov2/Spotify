package bg.sofia.uni.fmi.mjt.server.repository.resources;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerResourcesTest {

    private ServerResources serverResources;
    private SelectionKey mockKey;

    @BeforeEach
    void setUp() {
        serverResources = new ServerResources();
        mockKey = mock(SelectionKey.class);
        serverResources.setSelectionKey(mockKey);
    }

    @Test
    void testIsUserLoggedWithNoAttachment() {
        when(mockKey.attachment()).thenReturn(null);

        assertFalse(serverResources.isUserLogged(), "False is expected when the attachment is null!");
    }

    @Test
    void testSetSelectionKeyWithNullKey() {
        assertThrows(IllegalArgumentException.class, () -> serverResources.setSelectionKey(null),
                "IllegalArgumentException is expected when setting the selection key to null!");
    }

    @Test
    void testIsUserLoggedWithAttachment() {
        when(mockKey.attachment()).thenReturn("attachment");

        assertTrue(serverResources.isUserLogged(), "True is expected when the attachment is not null!");
    }

    @Test
    void testGetLoggedUserReturnsCorrectLoggedUser() {
        String email = "test@abv.bg";
        User user = new User(email, "pass", new PlaylistsRepository());
        serverResources.getUsersRepository().add(user);

        when(mockKey.attachment()).thenReturn(email);

        assertEquals(user, serverResources.getLoggedUser(), "The logged user must be the same as the expected one!");
    }

    @Test
    void testGetLoggedUserWithoutLoggedUser() {
        when(mockKey.attachment()).thenReturn(null);

        assertNull(serverResources.getLoggedUser(), "The logged user must be null!");
    }

}

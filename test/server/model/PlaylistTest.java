package bg.sofia.uni.fmi.mjt.server.model;

import bg.sofia.uni.fmi.mjt.spotify.server.model.IdentifiableModel;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaylistTest {

    private static final String SONG_NAME1 = "songName1";

    private Playlist playlist;
    private Song song1;
    private Song song2;

    @BeforeEach
    void setUp() {
        song1 = new Song(SONG_NAME1, "artistName1", 0);
        song2 = new Song("songName2", "artistName2", 0);
        Map<String, Song> songMap = new HashMap<>();
        playlist = new Playlist("playlistName", songMap);
    }

    @Test
    void testAddSongWithUniqueSong() {
        assertTrue(playlist.addSong(song1), "The song must be added because it does not already exist!");
    }

    @Test
    void testAddSongWithExistingSong() {
        playlist.addSong(song1);
        assertFalse(playlist.addSong(song1), "The song must not be added because it already exist!");
    }

    @Test
    void testAddSongWithNullSong() {
        assertThrows(IllegalArgumentException.class, () -> playlist.addSong(null),
                "IllegalArgumentException is expected to be thrown when null song is passed as an argument!");
    }

    @Test
    void testRemoveSongWithExistingSong() {
        playlist.addSong(song1);
        assertTrue(playlist.removeSong(song1), "The song must be successfully removed!");
    }

    @Test
    void testRemoveSongWithNoExistingSong() {
        assertFalse(playlist.removeSong(song1), "The song must not be removed because it does not exist!");
    }

    @Test
    void testRemoveSongWithNullSong() {
        assertThrows(IllegalArgumentException.class, () -> playlist.removeSong(null),
                "IllegalArgumentException is expected to be thrown when null song is passed as an argument!");
    }

    @Test
    void testContainsWithExistingSong() {
        playlist.addSong(song1);

        String errMess = "The contains method must return true if the song is in the playlist!";
        assertTrue(playlist.contains(SONG_NAME1), errMess);
    }

    @Test
    void testContainsWithNotExistingSong() {
        String errMess = "The contains method must return false if the song is not in the playlist!";
        assertFalse(playlist.contains(SONG_NAME1), errMess);
    }

    @Test
    void testContainsWithNullSong() {
        assertThrows(IllegalArgumentException.class, () -> playlist.contains(null),
                "IllegalArgumentException is expected to be thrown when null song is passed as an argument!");
    }

    @Test
    void testIncrementPlayingsCountOf() {
        playlist.addSong(song1);
        playlist.incrementPlayingsCountOf(SONG_NAME1);
        assertEquals(1, song1.getPlayCount(), "The playings count must be 1!");
    }

    @Test
    void testDisplayListOfSongsWithSongs() {
        playlist.addSong(song1);
        playlist.addSong(song2);

        String expectedOutput = "Songs: " + System.lineSeparator() +
                "1. Title: songName1, Singer: artistName1" + System.lineSeparator() +
                "2. Title: songName2, Singer: artistName2";
        String actualOutput = Playlist.displayListOfSongs(List.of(song1, song2));

        assertEquals(expectedOutput, actualOutput, "The two outputs must be equal but are not!");
    }

    @Test
    void testDisplayListOfSongsWithoutSongs() {
        String expectedOutput = "No songs found!";
        String actualOutput = Playlist.displayListOfSongs(List.of());

        assertEquals(expectedOutput, actualOutput, "The two outputs must be equal but are not!");
    }

    @Test
    void testIsDuplicateWithExistingPlaylist() {
        Map<String, IdentifiableModel> playlists = new HashMap<>();
        playlists.put(playlist.getId(), playlist);
        assertTrue(playlist.isDuplicate(playlists), "True is expected when the playlist is in the repository!");
    }

    @Test
    void testIsDuplicateWithNotExistingPlaylist() {
        Map<String, IdentifiableModel> playlists = new HashMap<>();
        assertFalse(playlist.isDuplicate(playlists), "False is expected when the playlist is not in the repository!");
    }

}

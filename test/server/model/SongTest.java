package bg.sofia.uni.fmi.mjt.server.model;

import bg.sofia.uni.fmi.mjt.spotify.server.model.IdentifiableModel;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SongTest {

    private Song song1;

    @BeforeEach
    public void setUp() {
        song1 = new Song("songName", "artistName", 0);
    }

    @Test
    public void testIncrementPlayCount() {
        song1.incrementPlayCount();
        assertEquals(1, song1.getPlayCount(), "The playings count must be equal to 1!");
    }

    @Test
    public void testToString() {
        assertEquals("Title: songName, Singer: artistName", song1.toString());
    }

    @Test
    public void testIsDuplicateWithExistingSong() {
        Map<String, IdentifiableModel> songMap = new HashMap<>();
        songMap.put(song1.getId(), song1);

        assertTrue(song1.isDuplicate(songMap),
                "Is duplicate method is expected to return true if the song already exist!");
    }

    @Test
    public void testIsDuplicateWithNotExistingSong() {
        Map<String, IdentifiableModel> songMap = new HashMap<>();

        assertFalse(song1.isDuplicate(songMap),
                "Is duplicate method is expected to return false if the song does not already exist!");
    }

}

package bg.sofia.uni.fmi.mjt.server.repository.comparator;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.comparator.SongPlayCountComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SongPlayCountComparatorTest {

    private final SongPlayCountComparator comparator = new SongPlayCountComparator();
    private Song song1;
    private Song song2;
    private int result;

    @BeforeEach
    void setUp() {
        song1 = new Song("songName1", "artistName1", 0);
        song2 = new Song("songName2", "artistName2", 0);
    }

    @Test
    void testCompare_WhenFirstSongHasMorePlaysThanSecond() {
        song1.incrementPlayCount();

        result = comparator.compare(song1, song2);
        assertTrue(result < 0, "The result must be less than zero but is not!");
    }

    @Test
    void testCompare_WhenFirstSongHasFewerPlaysThanSecond() {
        song2.incrementPlayCount();

        result = comparator.compare(song1, song2);
        assertTrue(result > 0, "The result must be greater than zero but is not!");
    }

    @Test
    void testCompare_WhenSongsHaveEqualPlays() {
        assertEquals(0, result, "The result must equal to zero but is not!");
    }

}

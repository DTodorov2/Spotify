package bg.sofia.uni.fmi.mjt.server.repository.song;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SongsRepositoryTest {

    private SongsRepository songsRepository;
    private Path tempFile;

    private Song song1;
    private Song song3;

    @BeforeEach
    void setUp() {
        songsRepository = new SongsRepository();

        song1 = new Song("Despacito", "Luis Fonsi", 0);
        Song song2 = new Song("Shape of You", "Ed Sheeran", 1);
        song3 = new Song("Bohemian Rhapsody", "Queen", 2);

        songsRepository.add(song1);
        songsRepository.add(song2);
        songsRepository.add(song3);
    }

    @Test
    void testSearchByWordsReturnsCorrectResults() {
        List<Song> result = songsRepository.searchByWords("Shape", "Ed");

        assertEquals(1, result.size(), "One song is expected to be returned!");
    }

    @Test
    void testGetTopStatisticsReturnsSortedByPlayCount() {
        List<Song> topSongs = songsRepository.getTopStatistics(2);

        assertEquals(2, topSongs.size(), "Two songs are expected to be returned!");
    }

    @Test
    void testGetTopStatisticsWhenRequestedMoreThanAvailable() {
        List<Song> topSongs = songsRepository.getTopStatistics(10);

        assertEquals(3, topSongs.size(), "All songs are expected to be returned!");
    }

    @Test
    void testGetSongByNameReturnsCorrectSong() {
        Song result = songsRepository.getSongByName("BohemianRhapsody");

        assertEquals(song3, result, "The returned song is expected to be the same as song3!");
    }

    @Test
    void testGetSongByNameReturnsNullIfNotFound() {
        Song result = songsRepository.getSongByName("NotExistingSong");

        assertNull(result, "No song should be returned!");
    }


    @Test
    void testSaveAndLoadDataWorksCorrectly() throws Exception {
        tempFile = Files.createTempFile("songs-test", ".json");

        songsRepository.saveData(tempFile);

        SongsRepository loadedRepo = new SongsRepository();
        loadedRepo.loadData(tempFile);

        Song loaded = loadedRepo.getSongByName("Despacito");

        assertEquals(song1, loaded, "One song is expected to be loaded!");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

}

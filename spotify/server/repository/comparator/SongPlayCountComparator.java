package bg.sofia.uni.fmi.mjt.spotify.server.repository.comparator;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;

import java.util.Comparator;

public class SongPlayCountComparator implements Comparator<Song> {

    @Override
    public int compare(Song song1, Song song2) {
        return Integer.compare(song2.getPlayCount(), song1.getPlayCount());
    }

}

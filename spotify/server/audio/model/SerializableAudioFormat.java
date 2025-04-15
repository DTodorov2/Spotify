package bg.sofia.uni.fmi.mjt.spotify.server.audio.model;

import javax.sound.sampled.AudioFormat;

import java.io.Serializable;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public record SerializableAudioFormat(String encoding, float sampleRate, int sampleSizeInBits,
                                        int channels, int frameSize, float frameRate, boolean bigEndian)
                                        implements Serializable {

    public static SerializableAudioFormat of(AudioFormat audioFormat) {
        checkArgumentNotNull(audioFormat, "audio format");

        return new SerializableAudioFormat(audioFormat.getEncoding().toString(), audioFormat.getSampleRate(),
                audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(),
                audioFormat.getFrameRate(), audioFormat.isBigEndian());
    }

    public AudioFormat toAudioFormat() {
        return new AudioFormat(new AudioFormat.Encoding(encoding),
                sampleRate(),
                sampleSizeInBits(),
                channels(),
                frameSize(),
                frameRate(),
                bigEndian());
    }
}

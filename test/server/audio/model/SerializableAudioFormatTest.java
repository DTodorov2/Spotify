package bg.sofia.uni.fmi.mjt.server.audio.model;

import bg.sofia.uni.fmi.mjt.spotify.server.audio.model.SerializableAudioFormat;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerializableAudioFormatTest {

    @Test
    public void testOfCreatesCorrectSerializableAudioFormat() {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        AudioFormat original = new AudioFormat(encoding, 44100.0f, 16,
                                      2, 4, 44100.0f, true);

        SerializableAudioFormat result = SerializableAudioFormat.of(original);

        assertEquals(encoding.toString(), result.encoding(),
                "The encoding should be the same but is not!");
        assertEquals(original.getSampleRate(), result.sampleRate(),
                "The sample rate should be the same but is not!");
        assertEquals(original.getSampleSizeInBits(), result.sampleSizeInBits(),
                "The sample size in bits should be the same but is not!");
        assertEquals(original.getChannels(), result.channels(),
                "The channels should be the same but is not!");
        assertEquals(original.getFrameSize(), result.frameSize(),
                "The frame size should be the same but is not!");
        assertEquals(original.getFrameRate(), result.frameRate(),
                "The frame rate should be the same but is not!");
        assertTrue(result.bigEndian(), "Big endian is expected to be true but is false!");
    }

    @Test
    public void testToAudioFormatReturnsCorrectAudioFormat() {
        SerializableAudioFormat serializable = new SerializableAudioFormat("PCM_SIGNED",
                                                                    44100.0f,
                                                                    16,
                                                                    2,
                                                                    4,
                                                                    44100.0f,
                                                                    true);
        AudioFormat audioFormat = serializable.toAudioFormat();

        assertEquals(serializable.encoding(), audioFormat.getEncoding().toString(),
                "The encoding should be the same but is not!");
        assertEquals(serializable.sampleRate(), audioFormat.getSampleRate(),
                "The sample rate should be the same but is not!");
        assertEquals(serializable.sampleSizeInBits(), audioFormat.getSampleSizeInBits(),
                "The sample size in bits should be the same but is not!");
        assertEquals(serializable.channels(), audioFormat.getChannels(),
                "The channels should be the same but is not!");
        assertEquals(serializable.frameSize(), audioFormat.getFrameSize(),
                "The frame size should be the same but is not!");
        assertEquals(serializable.frameRate(), audioFormat.getFrameRate(),
                "The frame rate should be the same but is not!");
        assertTrue(audioFormat.isBigEndian(), "Big endian is expected to be true but is false!");
    }

    @Test
    public void testOfWithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> SerializableAudioFormat.of(null),
                "IllegalArgumentException is expected when null argument is passed!");
    }

}

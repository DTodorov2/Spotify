package bg.sofia.uni.fmi.mjt.spotify.client.audio;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelConnectionException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.model.SerializableAudioFormat;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class AudioClient implements Runnable {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private static final int BUFFER_CAPACITY = 4096;

    private SocketChannel socketChannel;
    private final ByteBuffer buffer;

    private final ChannelDataWriter channelDataWriter;
    private final ChannelDataLoader channelDataLoader;

    private final ClientResources clientResources;
    private volatile boolean isRunning;

    private final String songName;

    public AudioClient(String songName, ClientResources clientResources) {
        checkArgumentNotNull(clientResources, "client resources");
        validateString(songName, "song name");

        prepareSocketChannel();
        buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
        channelDataLoader = new ChannelDataLoader(socketChannel);
        channelDataWriter = new ChannelDataWriter(socketChannel);
        isRunning = true;
        this.clientResources = clientResources;
        this.songName = songName;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    private void prepareSocketChannel() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(HOST, PORT));
        } catch (IOException e) {
            throw new ChannelConnectionException("Unable to connect the streaming channel to the server!", e);
        }

    }

    @Override
    public void run() {
        try {
            channelDataWriter.saveData("play " + songName);
            clientResources.setStreaming(true);
            startStreamingLoop(getSourceDataLine());
            channelDataWriter.saveData("stop");
            if (socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e)  {
            System.out.println("Cannot close the streaming channel!" + e.getMessage());
        } catch (SerializationDataException | DeserializationDataException e) {
            System.out.println(e.getMessage());
        } finally {
            isRunning = false;
            clientResources.setStreaming(false);
            clientResources.setAudioClient(null);
        }
    }

    private SourceDataLine getSourceDataLine() throws DeserializationDataException {
        try {
            AudioFormat format = readFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open();
            sourceDataLine.start();

            return sourceDataLine;
        } catch (LineUnavailableException e) {
            throw new DeserializationDataException("Unable to open an audio line!", e);
        }
    }

    private AudioFormat readFormat() throws DeserializationDataException {
        try {
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead == -1) {
                throw new EOFException("Reached end of stream before receiving format.");
            }

            buffer.flip();
            byte[] readBytes = new byte[buffer.remaining()];
            buffer.get(readBytes);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(readBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            SerializableAudioFormat audioFormat = (SerializableAudioFormat) objectInputStream.readObject();

            return audioFormat.toAudioFormat();

        } catch (ClassNotFoundException | IOException e) {
            throw new DeserializationDataException("Unable to get the format of the song!", e);
        }

    }

    private void startStreamingLoop(SourceDataLine sourceDataLine) throws DeserializationDataException {
        checkArgumentNotNull(sourceDataLine, "source data line");
        while (isRunning) {
            byte[] data = channelDataLoader.loadData().getBytes(StandardCharsets.UTF_8);

            if (data.length > 0) {
                sourceDataLine.write(data, 0, data.length);
            } else {
                isRunning = false;
            }
        }
        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();
    }
}

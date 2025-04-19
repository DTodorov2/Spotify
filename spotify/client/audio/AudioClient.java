package bg.sofia.uni.fmi.mjt.spotify.client.audio;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelConnectionException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.MarshallingException;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.model.SerializableAudioFormat;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.ERROR_ATTACH;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class AudioClient implements Runnable {

    private static final int SERVER_PORT = 8080;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_CAPACITY = 4096;
    private static final String STOP_COMMAND = "stop";
    private static final String PLAY_COMMAND = "play ";
    private static final String SEND_COMMAND = "send";

    private volatile boolean isRunning;
    private final ByteBuffer buffer;
    private SocketChannel socketChannel;
    private final ChannelDataLoader channelReader;
    private final ChannelDataWriter channelWriter;

    private final String songName;
    private final ClientResources clientResources;

    public AudioClient(String songName, ClientResources clientResources) throws ChannelConnectionException {
        checkArgumentNotNull(songName, "song key");
        checkArgumentNotNull(clientResources, "client state");

        prepareSocketChannel();

        this.buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
        this.channelReader = new ChannelDataLoader(socketChannel);
        this.channelWriter = new ChannelDataWriter(socketChannel);
        this.isRunning = true;
        this.clientResources = clientResources;
        this.songName = songName;
    }

    @Override
    public void run() {
        try {
            String response = channelReader.loadData();
            if (response.equals(ERROR_ATTACH)) {
                System.out.println("There was an error while streaming the song!");
                goToDefaultState();
                return;
            }
            clientResources.setStreaming(true);
            channelWriter.saveData(PLAY_COMMAND + songName);
            startStreamingLoop(getSourceDataLine());
            channelWriter.saveData(STOP_COMMAND);

        } catch (IOException | MarshallingException e) {
            System.out.println(e.getMessage());
        } finally {
            goToDefaultState();
        }
    }

    private void goToDefaultState() {
        try {
            isRunning = false;
            clientResources.setStreaming(false);
            clientResources.setAudioClient(null);
            if (socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            System.out.println("Unable to close the socket channel! " + e.getMessage());
        }

    }

    private void prepareSocketChannel() throws ChannelConnectionException {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        } catch (IOException e) {
            throw new ChannelConnectionException("Unable to connect the streaming channel to the server!", e);
        }
    }

    public SocketChannel getChannel() {
        return socketChannel;
    }

    private void startStreamingLoop(SourceDataLine sourceLine) throws IOException, MarshallingException {
        while (isRunning) {
            channelWriter.saveData(SEND_COMMAND);

            byte[] audioData = channelReader.loadBytes();

            if (audioData != null && audioData.length > 0) {
                sourceLine.write(audioData, 0, audioData.length);
            } else {
                isRunning = false;
            }
        }

        sourceLine.drain();
        sourceLine.stop();
        sourceLine.close();
    }

    private SourceDataLine getSourceDataLine() throws DeserializationDataException {
        try {
            AudioFormat format = readFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open();
            sourceDataLine.start();

            return sourceDataLine;
        } catch (ClassNotFoundException | LineUnavailableException | IOException e) {
            throw new DeserializationDataException("Unable to open an audio line!", e);
        }
    }

    private AudioFormat readFormat() throws IOException, ClassNotFoundException {
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        int size = buffer.getInt();
        byte[] formatBytes = new byte[size];
        buffer.get(formatBytes);

        try (ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(formatBytes))) {
            SerializableAudioFormat format = (SerializableAudioFormat) objectIn.readObject();
            return format.toAudioFormat();
        }
    }

    public void stopStreaming() {
        if (clientResources.isStreaming() ) {
            isRunning = false;
            clientResources.setStreaming(false);
            clientResources.setAudioClient(null);
        }
    }

}
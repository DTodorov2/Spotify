package bg.sofia.uni.fmi.mjt.spotify.client.audio;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelConnectionException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class AudioClient implements Runnable {

    private static final int SERVER_PORT = 8080;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_CAPACITY = 4096 ;

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
            if (response.equals("ERROR")) {
                System.out.println("There was an error while streaming the song!");
                goToDefaultState();
                return;
            }
            clientResources.setStreaming(true);
            channelWriter.saveData("play " + songName);
            //tuka moga li da vurna response-a na server-a -> govorq za tova,
            // ako shte trqq da napishe i artista primerno
            startStreamingLoop(getSourceDataLine());
            channelWriter.saveData("stop");

        } catch (IOException | SerializationDataException | DeserializationDataException e) {
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

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
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

    private void startStreamingLoop(SourceDataLine sourceLine) throws IOException, SerializationDataException, DeserializationDataException {
        while (isRunning) {
            channelWriter.saveData("send");
            //channelWriter.writeTo(socketChannel, "stream");

            byte[] audioData = channelReader.loadBytes(); //channelReader.readBytesFrom(socketChannel);

            if (audioData.length > 0) {
                sourceLine.write(audioData, 0, audioData.length);
            } else {
                isRunning = false;
            }
        }

        sourceLine.drain();
        sourceLine.stop();
        sourceLine.close();
    }

//    private void start(SourceDataLine sourceLine) throws IOException, SerializationDataException,
//                                                             DeserializationDataException {
//
//        checkArgumentNotNull(sourceLine, "source line");
//
//        while (isRunning) {
//            channelWriter.saveData("send");
//            byte[] data = channelReader.loadBytes();
//
//            if (data.length < 1) {
//                isRunning = false;
//                break;
//            }
//
//            sourceLine.write(data, 0, data.length);
//        }
//
//        sourceLine.drain();
//        sourceLine.stop();
//        sourceLine.close();
//
//    }

    private SourceDataLine getSourceDataLine() throws DeserializationDataException {
        try {
            AudioFormat format = readFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open();
            sourceDataLine.start();

            return sourceDataLine;
        } catch (LineUnavailableException | IOException e) {
            throw new DeserializationDataException("Unable to open an audio line!", e);
        }
    }

    private AudioFormat readFormat() throws IOException {
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        float sampleSize = buffer.getFloat();
        int sampleSizeInBits = buffer.getInt();
        int channels = buffer.getInt();
        int frameSize = buffer.getInt();
        float frameRate = buffer.getFloat();
        boolean bigEndian = buffer.get() == 1;
        int encodingLength = buffer.getInt();
        byte[] encodingBytes = new byte[encodingLength];

        buffer.get(encodingBytes);
        String encodingStr = new String(encodingBytes, StandardCharsets.UTF_8);
        AudioFormat.Encoding encoding = new AudioFormat.Encoding(encodingStr);

        return new AudioFormat(encoding, sampleSize, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
    }
}
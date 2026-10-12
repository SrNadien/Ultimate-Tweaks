package nadiendev.ultimatetweaks.client.discord;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class DiscordIpc implements Closeable {

    private static final int OP_HANDSHAKE = 0;
    private static final int OP_FRAME = 1;
    private static final int OP_CLOSE = 2;
    private static final int OP_PING = 3;
    private static final int OP_PONG = 4;

    private static final int MAX_FRAME = 1 << 20;
    private static final int MAX_PIPE_INDEX = 10;
    private static final int MAX_REPLY_FRAMES = 16;

    private final Transport transport;
    private final long processId;
    private boolean closed;

    DiscordIpc(Transport transport) {
        this.transport = transport;
        this.processId = ProcessHandle.current().pid();
    }

    public static DiscordIpc connect(String applicationId) throws IOException {
        IOException last = null;

        for (int index = 0; index < MAX_PIPE_INDEX; index++) {
            for (String path : candidatePaths(index)) {
                Transport transport = null;
                try {
                    transport = open(path);
                    DiscordIpc ipc = new DiscordIpc(transport);
                    ipc.handshake(applicationId);
                    return ipc;
                } catch (IOException exception) {
                    last = exception;
                    closeQuietly(transport);
                }
            }
        }

        throw last != null ? last : new IOException("Discord IPC no disponible");
    }

    public synchronized JsonObject setActivity(JsonObject activity) throws IOException {
        JsonObject args = new JsonObject();
        args.addProperty("pid", processId);
        args.add("activity", activity);
        return command("SET_ACTIVITY", args);
    }

    public synchronized JsonObject clearActivity() throws IOException {
        JsonObject args = new JsonObject();
        args.addProperty("pid", processId);
        return command("SET_ACTIVITY", args);
    }

    @Override
    public synchronized void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            writeFrame(OP_CLOSE, "{}");
        } catch (IOException ignored) {
        }
        closeQuietly(transport);
    }

    void handshake(String applicationId) throws IOException {
        JsonObject hello = new JsonObject();
        hello.addProperty("v", 1);
        hello.addProperty("client_id", applicationId);
        writeFrame(OP_HANDSHAKE, hello.toString());

        for (int i = 0; i < MAX_REPLY_FRAMES; i++) {
            Frame frame = readFrame();
            if (frame.opcode() == OP_CLOSE) {
                throw new IOException("Discord cerro la conexion: " + frame.body());
            }
            if (frame.opcode() != OP_FRAME) {
                continue;
            }
            JsonObject reply = parse(frame.body());
            String event = string(reply, "evt");
            if ("READY".equals(event)) {
                return;
            }
            if ("ERROR".equals(event)) {
                throw new IOException("Discord rechazo el handshake: " + errorMessage(reply));
            }
        }
        throw new IOException("Discord no respondio al handshake");
    }

    private JsonObject command(String name, JsonObject args) throws IOException {
        if (closed) {
            throw new IOException("Conexion cerrada");
        }

        String nonce = UUID.randomUUID().toString();
        JsonObject request = new JsonObject();
        request.addProperty("cmd", name);
        request.add("args", args);
        request.addProperty("nonce", nonce);
        writeFrame(OP_FRAME, request.toString());

        for (int i = 0; i < MAX_REPLY_FRAMES; i++) {
            Frame frame = readFrame();
            switch (frame.opcode()) {
                case OP_PING -> writeFrame(OP_PONG, frame.body());
                case OP_CLOSE -> throw new IOException("Discord cerro la conexion: " + frame.body());
                case OP_FRAME -> {
                    JsonObject reply = parse(frame.body());
                    if (!nonce.equals(string(reply, "nonce"))) {
                        continue;
                    }
                    if ("ERROR".equals(string(reply, "evt"))) {
                        throw new IOException("Discord rechazo el comando: " + errorMessage(reply));
                    }
                    return reply;
                }
                default -> {
                }
            }
        }
        throw new IOException("Discord no respondio al comando " + name);
    }

    private void writeFrame(int opcode, String body) throws IOException {
        byte[] payload = body.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(8 + payload.length).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(opcode);
        buffer.putInt(payload.length);
        buffer.put(payload);
        transport.write(buffer.array());
    }

    private Frame readFrame() throws IOException {
        ByteBuffer header = ByteBuffer.wrap(transport.read(8)).order(ByteOrder.LITTLE_ENDIAN);
        int opcode = header.getInt();
        int length = header.getInt();
        if (length < 0 || length > MAX_FRAME) {
            throw new IOException("Frame de Discord invalido: " + length);
        }
        return new Frame(opcode, new String(transport.read(length), StandardCharsets.UTF_8));
    }

    private static JsonObject parse(String body) throws IOException {
        try {
            JsonElement element = JsonParser.parseString(body);
            if (element.isJsonObject()) {
                return element.getAsJsonObject();
            }
        } catch (RuntimeException exception) {
            throw new IOException("Respuesta de Discord ilegible", exception);
        }
        throw new IOException("Respuesta de Discord inesperada");
    }

    private static String string(JsonObject object, String key) {
        JsonElement element = object.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsString() : "";
    }

    private static String errorMessage(JsonObject reply) {
        JsonElement data = reply.get("data");
        if (data != null && data.isJsonObject()) {
            String message = string(data.getAsJsonObject(), "message");
            if (!message.isEmpty()) {
                return message;
            }
        }
        return reply.toString();
    }

    private static boolean windows() {
        return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");
    }

    private static List<String> candidatePaths(int index) {
        List<String> paths = new ArrayList<>();
        String name = "discord-ipc-" + index;

        if (windows()) {
            paths.add("\\\\.\\pipe\\" + name);
            return paths;
        }

        List<String> bases = new ArrayList<>();
        String runtime = System.getenv("XDG_RUNTIME_DIR");
        if (runtime != null && !runtime.isBlank()) {
            bases.add(runtime);
            bases.add(runtime + "/app/com.discordapp.Discord");
            bases.add(runtime + "/snap.discord");
        }
        for (String variable : List.of("TMPDIR", "TMP", "TEMP")) {
            String value = System.getenv(variable);
            if (value != null && !value.isBlank()) {
                bases.add(value);
            }
        }
        bases.add("/tmp");

        for (String base : bases) {
            String trimmed = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
            paths.add(trimmed + "/" + name);
        }
        return paths;
    }

    private static Transport open(String path) throws IOException {
        if (windows()) {
            return new PipeTransport(new RandomAccessFile(path, "rw"));
        }
        return new SocketTransport(SocketChannel.open(UnixDomainSocketAddress.of(path)));
    }

    private static void closeQuietly(Transport transport) {
        if (transport == null) {
            return;
        }
        try {
            transport.close();
        } catch (IOException ignored) {
        }
    }

    private record Frame(int opcode, String body) {
    }

    interface Transport extends Closeable {

        void write(byte[] data) throws IOException;

        byte[] read(int length) throws IOException;
    }

    private record PipeTransport(RandomAccessFile file) implements Transport {

        @Override
        public void write(byte[] data) throws IOException {
            file.write(data);
        }

        @Override
        public byte[] read(int length) throws IOException {
            byte[] data = new byte[length];
            file.readFully(data);
            return data;
        }

        @Override
        public void close() throws IOException {
            file.close();
        }
    }

    private record SocketTransport(SocketChannel channel) implements Transport {

        @Override
        public void write(byte[] data) throws IOException {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }

        @Override
        public byte[] read(int length) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(length);
            while (buffer.hasRemaining()) {
                if (channel.read(buffer) < 0) {
                    throw new IOException("Conexion con Discord cerrada");
                }
            }
            return buffer.array();
        }

        @Override
        public void close() throws IOException {
            channel.close();
        }
    }
}

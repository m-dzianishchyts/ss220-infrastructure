package club.ss220.port.gameserver.tcp.support;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.spi.GameServerPort;
import club.ss220.core.spi.exception.GameServerPortException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractTcpGameServerPort implements GameServerPort {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    private static final int TIMEOUT_MS = 5000;
    private static final int HEADER_BYTES = 4; // 0x00, 0x83, short length (big-endian)
    private static final int REQUEST_PAD_BYTES = 5; // protocol pad bytes after header
    private static final int REPSONSE_PAD_BYTES = 1; // protocol pad bytes after header
    private static final int TRAILER_BYTES = 1; // trailing 0x00 at end of message
    private static final int MAX_MESSAGE_LENGTH = Short.MAX_VALUE;
    private static final String DATA_PROPERTY = "data";

    protected <T> T callServer(GameServerData gameServer, String command, TypeReference<T> typeRef) {
        return executeCommand(gameServer, command, typeRef);
    }

    protected <T> T executeCommand(GameServerData gameServer, String command, TypeReference<T> typeRef) {
        String fullCommand = buildCommand(gameServer, command);
        String censoredCommand = censorSecrets(fullCommand);
        log.debug("Executing topic '{}' on {}:{}", censoredCommand, gameServer.ip(), gameServer.port());

        try {
            byte[] responseBytes = sendReceiveData(gameServer, fullCommand);
            Object raw = processByondResponseBody(responseBytes).get(DATA_PROPERTY);
            return objectMapper.convertValue(raw, typeRef);

        } catch (JsonProcessingException e) {
            String message = "Error decoding response for command '" + censoredCommand + "'";
            throw new GameServerPortException(gameServer, message, e);
        } catch (IOException e) {
            String message = "Error executing command '" + censoredCommand + "'";
            throw new GameServerPortException(gameServer, message, e);
        } catch (IllegalArgumentException e) {
            String message = "Error converting response to " + typeRef + ", command '" + censoredCommand + "'";
            throw new GameServerPortException(gameServer, message, e);
        }
    }

    protected String buildCommand(GameServerData server, String command) {
        StringBuilder sb = new StringBuilder(command);
        if (server.key() != null && !server.key().isEmpty()) {
            sb.append("&key=").append(server.key());
        }
        sb.append("&format=json");
        return sb.toString();
    }

    protected byte[] sendReceiveData(GameServerData server, String command) throws IOException {
        try (Socket socket = new Socket()) {
            socket.setSoTimeout(TIMEOUT_MS);
            socket.connect(new InetSocketAddress(server.ip(), server.port()), TIMEOUT_MS);

            byte[] packet = preparePacket(command);
            socket.getOutputStream().write(packet);
            socket.getOutputStream().flush();

            InputStream in = socket.getInputStream();

            byte[] header = readExactly(in, HEADER_BYTES);
            ByteBuffer hb = ByteBuffer.wrap(header);
            hb.get(); // 0x00
            hb.get(); // 0x83
            short length = hb.getShort(); // holder + json payload + trailing 0x00

            int bodyLength = Short.toUnsignedInt(length);
            if (bodyLength > MAX_MESSAGE_LENGTH) {
                throw new IOException("Invalid body length: " + bodyLength);
            }

            byte[] body = readExactly(in, bodyLength);
            if (body.length <= REPSONSE_PAD_BYTES + TRAILER_BYTES) {
                return new byte[0];
            }
            return Arrays.copyOfRange(body, REPSONSE_PAD_BYTES, body.length - TRAILER_BYTES);
        } catch (SocketTimeoutException e) {
            throw new GameServerPortException(server, "Server " + server.fullName() + " is unavailable", e);
        }
    }

    private static byte[] readExactly(InputStream in, int length) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        byte[] buffer = new byte[length];
        dis.readFully(buffer);
        return buffer;
    }

    protected byte[] preparePacket(String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        int messageLength = REQUEST_PAD_BYTES + dataBytes.length + TRAILER_BYTES; // holder + payload + trailing 0x00
        if (messageLength > MAX_MESSAGE_LENGTH) {
            String message = "Message '" + censorSecrets(data) + "' exceeds maximum length:" + MAX_MESSAGE_LENGTH;
            throw new IllegalArgumentException(message);
        }

        ByteBuffer buffer = ByteBuffer.allocate(HEADER_BYTES + messageLength);
        buffer.put((byte) 0x00);
        buffer.put((byte) 0x83);
        buffer.putShort((short) (messageLength));
        buffer.put(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00});
        buffer.put(dataBytes);
        buffer.put((byte) 0x00);
        return buffer.array();
    }

    protected Map<String, Object> processByondResponseBody(byte[] jsonBytes) throws JsonProcessingException {
        String jsonString = new String(jsonBytes, StandardCharsets.UTF_8);
        if (jsonString.isEmpty()) {
            return Map.of();
        }

        try {
            JsonNode node = objectMapper.readTree(jsonString);
            if (node.isArray()) {
                return Map.of(DATA_PROPERTY, objectMapper.convertValue(node, List.class));
            } else if (node.isObject()) {
                return Map.of(DATA_PROPERTY, objectMapper.convertValue(node, Map.class));
            }

            return Map.of(DATA_PROPERTY, new Object());
        } catch (JsonProcessingException e) {
            log.error("Error decoding BYOND response, json=\"{}\"", jsonString);
            throw e;
        }
    }

    private String censorSecrets(String command) {
        if (command == null || command.isEmpty()) {
            return command;
        }
        return command.replaceAll("(?i)([?&]key=)([^&]+)", "$1****");
    }
}

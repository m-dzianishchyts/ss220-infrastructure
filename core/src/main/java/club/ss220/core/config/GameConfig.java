package club.ss220.core.config;

import club.ss220.core.shared.GameServerData;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@Configuration
@ConfigurationProperties("application.integration.game")
public class GameConfig {

    public static final String BUILD_PARADISE = "paradise";
    public static final String BUILD_BANDASTRATION = "bandastation";

    @Valid
    private List<GameServerData> servers;

    @PostConstruct
    void resolveHosts() throws UnknownHostException {
        for (GameServerData server : servers) {
            log.debug("Post-processing server {}", server);
            if (server.host() != null) {
                Optional<Inet4Address> optionalIPv4 = Arrays.stream(InetAddress.getAllByName(server.host()))
                        .filter(Inet4Address.class::isInstance)
                        .map(Inet4Address.class::cast)
                        .findAny();
                if (optionalIPv4.isPresent()) {
                    server.ip(optionalIPv4.get());
                    log.debug("Resolved host {}", server.ip());
                } else {
                    log.debug("Couldn't resolve host {}. Falling back to ip {}", server.host(), server.ip());
                }
            }
            if (server.ip() == null) {
                throw new IllegalArgumentException("Either host or ip must be provided for a game server");
            }
        }
    }

    public Optional<GameServerData> getServerById(String id) {
        return servers.stream()
                .filter(s -> id.equals(s.id()))
                .findAny();
    }

    public Optional<GameServerData> getServerByAddress(String host, Integer port) {
        return servers.stream()
                .filter(s -> s.port().equals(port) && (host.equals(s.ip().getHostAddress()) || host.equals(s.host())))
                .findAny();
    }
}

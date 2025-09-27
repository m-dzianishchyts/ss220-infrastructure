package club.ss220.core.config;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.GameBuildData;
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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static club.ss220.core.shared.GameBuild.BANDASTATION;
import static club.ss220.core.shared.GameBuild.PARADISE;

@Slf4j
@Data
@Configuration
@ConfigurationProperties("ss220.integration.game")
public class GameConfig {

    public static final String BUILD_PARADISE = "paradise";
    public static final String BUILD_BANDASTRATION = "bandastation";

    @Valid
    private Map<GameBuild, List<GameServerData>> servers = new EnumMap<>(GameBuild.class);
    @Valid
    private Map<GameBuild, GameBuildData> builds = Map.of(
            BANDASTATION, GameBuildData.disabled(),
            PARADISE, GameBuildData.disabled()
    );

    @PostConstruct
    void completeServerData() throws UnknownHostException {
        completeServerBuilds();
        resolveServerHosts();
    }

    public List<GameServerData> getSupportedServers() {
        return getServersFlat().stream()
                .filter(s -> builds.get(s.build()).isEnabled())
                .toList();
    }

    public Optional<GameServerData> getServerByAddress(String host, Integer port) {
        return getServersFlat().stream()
                .filter(s -> s.port().equals(port) && (host.equals(s.ip().getHostAddress()) || host.equals(s.host())))
                .findAny();
    }

    public Optional<GameServerData> getServerById(String id) {
        return getServersFlat().stream()
                .filter(s -> id.equals(s.id()))
                .findAny();
    }

    private void completeServerBuilds() {
        servers.forEach((build, buildServers) -> buildServers.forEach(server -> server.build(build)));
    }

    private void resolveServerHosts() throws UnknownHostException {
        for (GameServerData server : getSupportedServers()) {
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

    private List<GameServerData> getServersFlat() {
        return servers.values().stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .toList();
    }
}

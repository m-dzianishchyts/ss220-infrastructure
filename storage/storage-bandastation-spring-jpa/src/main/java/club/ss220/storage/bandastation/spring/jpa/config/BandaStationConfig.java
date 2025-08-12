package club.ss220.storage.bandastation.spring.jpa.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import jakarta.annotation.PostConstruct;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Configuration
@RequiredArgsConstructor
public class BandaStationConfig {

    private static final String ROLES_RESOURCE = "classpath:roles.yml";

    private final ResourceLoader resourceLoader;

    @NotNull
    private Map<String, List<String>> roles;

    @PostConstruct
    void loadRolesFromYaml() {
        try (InputStream is = resourceLoader.getResource(ROLES_RESOURCE).getInputStream()) {
            Map<String, Map<String, List<String>>> root = new Yaml().load(is);
            Map<String, List<String>> parsed = root.getOrDefault("roles", Map.of());

            roles = parsed.entrySet().stream().collect(Collectors.toUnmodifiableMap(
                    Map.Entry::getKey,
                    e -> e.getValue() != null ? e.getValue() : List.of()
            ));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

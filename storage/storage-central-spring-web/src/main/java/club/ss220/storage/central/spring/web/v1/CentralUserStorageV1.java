package club.ss220.storage.central.spring.web.v1;

import club.ss220.core.shared.UserData;
import club.ss220.core.spi.UserStorage;
import club.ss220.core.util.ValidationService;
import club.ss220.storage.central.spring.web.mapper.UserMapper;
import club.ss220.storage.central.spring.web.v1.exception.CentralApiException;
import club.ss220.storage.central.spring.web.v1.presentation.UserPresentationV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CentralUserStorageV1 implements UserStorage {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final UserMapper userMapper;
    private final ValidationService validationService;

    public CentralUserStorageV1(@Value("${application.api.central.endpoint}") String baseUrl,
                                @Value("${application.api.central.token}") String token,
                                UserMapper userMapper, ValidationService validationService) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        this.restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        }));

        this.userMapper = userMapper;
        this.validationService = validationService;
    }

    public Optional<UserData> getUserByCkey(String ckey) {
        String url = baseUrl + "/players/ckey/{ckey}";
        try {
            return Optional.ofNullable(restTemplate.getForObject(url, UserPresentationV1.class, ckey))
                    .map(userMapper::toUserData)
                    .map(validationService::validate);

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("User with ckey {} not found", ckey);
            return Optional.empty();
        } catch (Exception e) {
            throw new CentralApiException("Error getting player by ckey: " + ckey, e);
        }
    }

    public Optional<UserData> getUserByDiscordId(long discordId) {
        String url = baseUrl + "/players/discord/{discordId}";
        try {
            return Optional.ofNullable(restTemplate.getForObject(url, UserPresentationV1.class, discordId))
                    .map(userMapper::toUserData)
                    .map(validationService::validate);

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("User with Discord ID {} not found", discordId);
            return Optional.empty();
        } catch (Exception e) {
            throw new CentralApiException("Error getting player by Discord ID: " + discordId, e);
        }
    }
}

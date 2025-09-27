package club.ss220.storage.central.spring.web.v1.user;

import club.ss220.core.shared.UserData;
import club.ss220.core.spi.UserStorage;
import club.ss220.core.util.ValidationService;
import club.ss220.storage.central.spring.web.v1.CentralApiV1;
import club.ss220.storage.central.spring.web.v1.exception.CentralApiException;
import club.ss220.storage.central.spring.web.v1.user.mapper.UserMapperV1;
import club.ss220.storage.central.spring.web.v1.user.presentation.UserPresentationV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(name = "ss220.api.central.endpoint")
public class CentralUserStorageV1 implements UserStorage {

    private final CentralApiV1 centralApi;
    private final UserMapperV1 mapper;
    private final ValidationService validationService;

    public CentralUserStorageV1(CentralApiV1 centralApi, UserMapperV1 mapper, ValidationService validationService) {
        this.centralApi = centralApi;
        this.mapper = mapper;
        this.validationService = validationService;
    }

    @Override
    public Optional<UserData> findUserById(int id) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = centralApi.resolveUrl("/players/{id}");
        try {
            return Optional.ofNullable(template.getForObject(url, UserPresentationV1.class, id))
                    .map(mapper::toUserData)
                    .map(validationService::validate);

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("User with ID {} not found", id);
            return Optional.empty();
        } catch (Exception e) {
            throw new CentralApiException("Error getting player by ID: " + id, e);
        }
    }

    public Optional<UserData> findUserByCkey(String ckey) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = centralApi.resolveUrl("/players/ckey/{ckey}");
        try {
            return Optional.ofNullable(template.getForObject(url, UserPresentationV1.class, ckey))
                    .map(mapper::toUserData)
                    .map(validationService::validate);

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("User with ckey {} not found", ckey);
            return Optional.empty();
        } catch (Exception e) {
            throw new CentralApiException("Error getting player by ckey: " + ckey, e);
        }
    }

    public Optional<UserData> findUserByDiscordId(long discordId) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = centralApi.resolveUrl("/players/discord/{discordId}");
        try {
            return Optional.ofNullable(template.getForObject(url, UserPresentationV1.class, discordId))
                    .map(mapper::toUserData)
                    .map(validationService::validate);

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("User with Discord ID {} not found", discordId);
            return Optional.empty();
        } catch (Exception e) {
            throw new CentralApiException("Error getting player by Discord ID: " + discordId, e);
        }
    }
}

package club.ss220.storage.central.spring.web.v1.whitelist;

import club.ss220.core.shared.NewWhitelistEntry;
import club.ss220.core.shared.WhitelistData;
import club.ss220.core.shared.exception.UserBlacklistedException;
import club.ss220.core.spi.WhitelistQuery;
import club.ss220.core.spi.WhitelistStorage;
import club.ss220.core.util.ValidationService;
import club.ss220.storage.central.spring.web.v1.CentralApiV1;
import club.ss220.storage.central.spring.web.v1.exception.CentralApiException;
import club.ss220.storage.central.spring.web.v1.presentation.PaginatedResponseV1;
import club.ss220.storage.central.spring.web.v1.whitelist.mapper.WhitelistMapperV1;
import club.ss220.storage.central.spring.web.v1.whitelist.presentation.NewWhitelistDiscordV1;
import club.ss220.storage.central.spring.web.v1.whitelist.presentation.WhitelistPresentationV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(name = "ss220.api.central.endpoint")
public class CentralWhitelistStorageV1 implements WhitelistStorage {

    private final CentralApiV1 centralApi;
    private final WhitelistMapperV1 mapper;
    private final ValidationService validationService;

    public CentralWhitelistStorageV1(CentralApiV1 centralApi, WhitelistMapperV1 mapper,
                                     ValidationService validationService) {
        this.centralApi = centralApi;
        this.mapper = mapper;
        this.validationService = validationService;
    }

    @Override
    public WhitelistData save(NewWhitelistEntry request) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = UriComponentsBuilder.fromUriString(centralApi.resolveUrl("/whitelists"))
                .queryParamIfPresent("ignore_bans", Optional.ofNullable(request.ignoreBlacklist()))
                .toUriString();
        try {
            NewWhitelistDiscordV1 body = mapper.toNewWhitelistDiscord(request);
            WhitelistPresentationV1 created = template.postForObject(url, body, WhitelistPresentationV1.class);
            return Optional.ofNullable(created)
                    .map(mapper::toWhitelistData)
                    .map(validationService::validate)
                    .orElseThrow(() -> new CentralApiException("Empty response when creating whitelist"));
        } catch (HttpClientErrorException.Conflict e) {
            throw new UserBlacklistedException(request.playerDiscordId(), e);
        } catch (Exception e) {
            throw new CentralApiException("Error creating whitelist for player=" + request.playerDiscordId() + ", serverType=" + request.serverType(), e);
        }
    }

    @Override
    public Optional<WhitelistData> findById(Integer id) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = centralApi.resolveUrl("/whitelists/{id}");
        try {
            return Optional.ofNullable(template.getForObject(url, WhitelistPresentationV1.class, id))
                    .map(mapper::toWhitelistData)
                    .map(validationService::validate);

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("Whitelist with id {} not found", id);
            return Optional.empty();
        } catch (Exception e) {
            throw new CentralApiException("Error getting whitelist by id: " + id, e);
        }
    }

    @Override
    public List<WhitelistData> findByQuery(WhitelistQuery query) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = UriComponentsBuilder.fromUriString(centralApi.resolveUrl("/whitelists"))
                .queryParams(whitelistQueryParams(query))
                .toUriString();
        try {
            var typeRef = new ParameterizedTypeReference<PaginatedResponseV1<WhitelistPresentationV1>>() {};
            var response = template.exchange(url, HttpMethod.GET, null, typeRef);

            return Optional.ofNullable(response.getBody())
                    .map(PaginatedResponseV1::items)
                    .orElseGet(List::of).stream()
                    .map(mapper::toWhitelistData)
                    .map(validationService::validate)
                    .sorted(Comparator.comparing(WhitelistData::issueDateTime).reversed())
                    .toList();

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("Whitelists not found for query: {}", query);
            return List.of();
        } catch (Exception e) {
            throw new CentralApiException("Error getting whitelists for query: " + query, e);
        }
    }

    private static MultiValueMap<String, String> whitelistQueryParams(WhitelistQuery query) {
        Map<String, Object> params = new HashMap<>();
        params.put("discord_id", query.getPlayerDiscordId());
        params.put("admin_discord_id", query.getAdminDiscordId());
        params.put("server_type", query.getServerType());
        params.put("active_only", query.getActiveOnly());
        params.put("page", query.getPage() + 1);
        params.put("page_size", query.getPageSize());

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        params.forEach((k, v) -> Optional.ofNullable(v).ifPresent(_ -> queryParams.add(k, v.toString())));
        return queryParams;
    }
}

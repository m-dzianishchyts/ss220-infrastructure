package club.ss220.storage.central.spring.web.v1.blacklist;

import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.core.shared.NewBlacklistEntry;
import club.ss220.core.spi.BlacklistQuery;
import club.ss220.core.spi.BlacklistStorage;
import club.ss220.core.util.ValidationService;
import club.ss220.storage.central.spring.web.v1.CentralApiV1;
import club.ss220.storage.central.spring.web.v1.blacklist.mapper.BlacklistMapperV1;
import club.ss220.storage.central.spring.web.v1.blacklist.presentation.BlacklistPresentationV1;
import club.ss220.storage.central.spring.web.v1.blacklist.presentation.NewBlacklistEntryDiscordIdV1;
import club.ss220.storage.central.spring.web.v1.exception.CentralApiException;
import club.ss220.storage.central.spring.web.v1.presentation.PaginatedResponseV1;
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
public class CentralBlacklistStorageV1 implements BlacklistStorage {

    private final CentralApiV1 centralApi;
    private final BlacklistMapperV1 mapper;
    private final ValidationService validationService;

    public CentralBlacklistStorageV1(CentralApiV1 centralApi, BlacklistMapperV1 mapper,
                                     ValidationService validationService) {
        this.centralApi = centralApi;
        this.mapper = mapper;
        this.validationService = validationService;
    }

    @Override
    public BlacklistEntryData save(NewBlacklistEntry request) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = UriComponentsBuilder.fromUriString(centralApi.resolveUrl("/whitelist_bans"))
                .queryParamIfPresent("invalidate_wls", Optional.ofNullable(request.invalidateWhitelist()))
                .toUriString();
        try {
            NewBlacklistEntryDiscordIdV1 body = mapper.toNewWhitelistEntry(request);
            BlacklistPresentationV1 created = template.postForObject(url, body, BlacklistPresentationV1.class);
            return Optional.ofNullable(created)
                    .map(mapper::toBlacklistEntryData)
                    .map(validationService::validate)
                    .orElseThrow(() -> new CentralApiException("Empty response when creating blacklist entry"));
        } catch (Exception e) {
            throw new CentralApiException("Error creating blacklist entry: " + request, e);
        }
    }

    @Override
    public Optional<BlacklistEntryData> findById(Integer id) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = centralApi.resolveUrl("/whitelist_bans/{id}");
        try {
            return Optional.ofNullable(template.getForObject(url, BlacklistPresentationV1.class, id))
                    .map(mapper::toBlacklistEntryData)
                    .map(validationService::validate);

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("Whitelist with id {} not found", id);
            return Optional.empty();
        } catch (Exception e) {
            throw new CentralApiException("Error getting whitelist by id: " + id, e);
        }
    }

    @Override
    public List<BlacklistEntryData> findByQuery(BlacklistQuery query) {
        RestTemplate template = centralApi.getRestTemplate();
        String url = UriComponentsBuilder.fromUriString(centralApi.resolveUrl("/whitelist_bans"))
                .queryParams(blacklistQueryParams(query))
                .toUriString();
        try {
            var typeRef = new ParameterizedTypeReference<PaginatedResponseV1<BlacklistPresentationV1>>() {};
            var response = template.exchange(url, HttpMethod.GET, null, typeRef);

            return Optional.ofNullable(response.getBody())
                    .map(PaginatedResponseV1::items)
                    .orElseGet(List::of).stream()
                    .map(mapper::toBlacklistEntryData)
                    .map(validationService::validate)
                    .sorted(Comparator.comparing(BlacklistEntryData::issueDateTime).reversed())
                    .toList();

        } catch (HttpClientErrorException.NotFound _) {
            log.debug("Whitelists not found for query: {}", query);
            return List.of();
        } catch (Exception e) {
            throw new CentralApiException("Error getting whitelists for query: " + query, e);
        }
    }

    private static MultiValueMap<String, String> blacklistQueryParams(BlacklistQuery query) {
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

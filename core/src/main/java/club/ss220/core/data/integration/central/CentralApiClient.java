package club.ss220.core.data.integration.central;

import reactor.core.publisher.Mono;

@Deprecated
public interface CentralApiClient {

    Mono<UserDto> getMemberByCkey(String ckey);

    Mono<UserDto> getMemberByDiscordId(long discordId);
}

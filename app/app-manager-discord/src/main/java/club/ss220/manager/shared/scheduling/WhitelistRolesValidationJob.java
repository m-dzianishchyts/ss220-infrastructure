package club.ss220.manager.shared.scheduling;

import club.ss220.core.application.GetWhitelistUseCase;
import club.ss220.core.shared.GameServerType;
import club.ss220.core.shared.UserData;
import club.ss220.core.shared.WhitelistData;
import club.ss220.core.shared.event.WhitelistUpdateEvent;
import club.ss220.core.spi.WhitelistQuery;
import club.ss220.manager.config.GameDiscordConfig;
import club.ss220.manager.config.GameServerTypeConfig;
import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.apache.commons.collections4.SetUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhitelistRolesValidationJob {

    @Nullable
    private JDA jda;
    private final GameDiscordConfig gameDiscordConfig;
    private final GetWhitelistUseCase getWhitelistUseCase;
    private final ApplicationEventPublisher eventPublisher;

    @BEventListener
    public void onReady(ReadyEvent event) {
        jda = event.getJDA();
    }

    @Scheduled(cron = "${ss220.schedules.whitelist-validation.cron:@hourly}")
    public void validateGuilds() {
        if (jda == null) {
            log.error("JDA is not ready, skipping job");
            return;
        }
        jda.getGuilds().forEach(this::validateGuild);
    }

    private void validateGuild(Guild guild) {
        long startTime = System.currentTimeMillis();
        gameDiscordConfig.getServerTypes().forEach(serverType -> validateWhitelistRoles(guild, serverType));
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("Guild '{}' ({}): validated whitelist roles in {}ms", guild.getName(), guild.getIdLong(), executionTime);
    }

    private void validateWhitelistRoles(Guild guild, GameServerType serverType) {
        Optional<GameServerTypeConfig> serverTypeConfig = gameDiscordConfig.getServerTypeConfig(serverType);
        if (serverTypeConfig.isEmpty()) {
            log.error("Unknown server type: {}. Skipping job", serverType);
            return;
        }
        Long roleId = serverTypeConfig.get().discordRoleId();
        if (roleId == null) {
            log.warn("No whitelist role, serveType: {}. Skipping job", serverType);
            return;
        }
        Role role = guild.getRoleById(roleId);
        long guildId = guild.getIdLong();
        if (role == null) {
            log.error("Guild {} has no whitelist role, serveType: {}. Skipping job", guildId, serverType);
            return;
        }

        int added = 0;
        int removed = 0;
        WhitelistQuery query = WhitelistQuery.builder().serverType(serverType.name()).activeOnly(true).build();
        List<WhitelistData> whitelist = getWhitelistUseCase.execute(query);

        Set<Long> whitelistedIds = whitelist.stream()
                .map(WhitelistData::playerData)
                .filter(Objects::nonNull)
                .map(UserData::discordId)
                .collect(Collectors.toSet());
        Set<Long> membersWithRoleIds = guild.getMembersWithRoles(role).stream()
                .map(Member::getIdLong)
                .collect(Collectors.toSet());

        Set<Long> toAdd = SetUtils.difference(whitelistedIds, membersWithRoleIds);
        Set<Long> toRemove = SetUtils.difference(membersWithRoleIds, whitelistedIds);

        for (Long userId : toAdd) {
            try {
                eventPublisher.publishEvent(WhitelistUpdateEvent.add(guild.getIdLong(), serverType, userId));
                added++;
            } catch (Exception e) {
                log.error("Failed to add whitelist role for user {} in guild {}, serverType: {}",
                          userId, guildId, serverType, e);
            }
        }
        for (Long userId : toRemove) {
            try {
                eventPublisher.publishEvent(WhitelistUpdateEvent.remove(guild.getIdLong(), serverType, userId));
                removed++;
            } catch (Exception e) {
                log.error("Failed to remove whitelist role for user {} in guild {}, serverType: {}",
                          userId, guildId, serverType, e);
            }
        }

        if (added > 0 || removed > 0) {
            log.info("Guild '{}' ({}): {} roles assigned, {} roles removed, serverType: {}",
                     guild.getName(), guildId, toAdd.size(), toRemove.size(), serverType);
        }
    }
}

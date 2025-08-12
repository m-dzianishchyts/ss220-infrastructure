package club.ss220.manager.service;

import club.ss220.core.application.GetMemberDataUseCase;
import club.ss220.core.shared.MemberData;
import club.ss220.manager.model.MemberTarget;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.utils.MiscUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MemberResolutionService {

    private final GetMemberDataUseCase getMemberDataUseCase;

    public Optional<MemberData> resolve(MemberTarget target) {
        log.debug("Resolving member target: {}", target);
        if (target.discordUser() != null) {
            return resolveByDiscordId(target.discordUser().getIdLong());
        }

        String query = target.query();
        if (query == null) {
            throw new IllegalArgumentException("Member target must have a discord user or a query");
        }

        if (query.startsWith("<@") && query.endsWith(">")) {
            query = query.substring(2, query.length() - 1);
        }

        try {
            long userId = MiscUtil.parseSnowflake(query);
            return resolveByDiscordId(userId);
        } catch (NumberFormatException e) {
            return resolveByCkey(query);
        }
    }

    private Optional<MemberData> resolveByDiscordId(Long userId) {
        Optional<MemberData> member = getMemberDataUseCase.execute(userId);
        if (member.isPresent()) {
            log.debug("Successfully resolved discord {} to member {}", userId, member.get().userData().id());
        } else {
            log.debug("No member found for discord: {}", userId);
        }

        return member;
    }

    private Optional<MemberData> resolveByCkey(String ckey) {
        Optional<MemberData> member = getMemberDataUseCase.execute(ckey);
        if (member.isPresent()) {
            log.debug("Successfully resolved ckey '{}' to member {}", ckey, member.get().userData().id());
        } else {
            log.debug("No member found for ckey: '{}'", ckey);
        }

        return member;
    }
}

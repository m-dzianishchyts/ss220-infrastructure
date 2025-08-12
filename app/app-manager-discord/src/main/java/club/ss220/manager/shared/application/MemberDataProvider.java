package club.ss220.manager.shared.application;

import club.ss220.core.application.GetMemberDataUseCase;
import club.ss220.core.shared.MemberData;
import club.ss220.manager.shared.MemberTarget;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MemberDataProvider {

    private final GetMemberDataUseCase getMemberDataUseCase;

    public Optional<MemberData> getByTarget(MemberTarget target) {
        if (target.discordId() != null) {
            return getByDiscordId(target.discordId());
        }
        if (target.ckey() != null) {
            return getByCkey(target.ckey());
        }
        throw new IllegalArgumentException("Member target must have a discordId or a ckey");
    }

    private Optional<MemberData> getByDiscordId(Long userId) {
        Optional<MemberData> member = getMemberDataUseCase.execute(userId);
        if (member.isPresent()) {
            log.debug("Successfully resolved discord {} to member {}", userId, member.get().userData().id());
        } else {
            log.debug("No member found for discord: {}", userId);
        }

        return member;
    }

    private Optional<MemberData> getByCkey(String ckey) {
        Optional<MemberData> member = getMemberDataUseCase.execute(ckey);
        if (member.isPresent()) {
            log.debug("Successfully resolved ckey {} to member {}", ckey, member.get().userData().id());
        } else {
            log.debug("No member found for ckey: {}", ckey);
        }

        return member;
    }
}

package club.ss220.manager.shared.application;

import club.ss220.manager.shared.GameServerType;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleAssignmentService {

    public void addWhitelistRole(Guild guild, long userId, GameServerType serverType) {
        Long roleId = serverType.discordRoleId();
        if (roleId == null) {
            log.warn("No role configured for server type: {}. Skipping role assignment", serverType);
            return;
        }
        Role role = guild.getRoleById(roleId);
        if (role == null) {
            log.error("Configured role {} not found in guild {}. Skipping role assignment", roleId, guild.getId());
            return;
        }

        guild.retrieveMemberById(userId).queue(
                (Member m) -> addRole(guild, m, role),
                err -> log.error("Cannot add role {} for member {}: {}", role, userId, err.getMessage())
        );
    }

    public void removeWhitelistRole(Guild guild, long userId, GameServerType serverType) {
        Long roleId = serverType.discordRoleId();
        if (roleId == null) {
            log.warn("No role configured for server type: {}. Skipping role removal", serverType);
            return;
        }
        Role role = guild.getRoleById(roleId);
        if (role == null) {
            log.error("Configured role {} not found in guild {}. Skipping role removal", roleId, guild.getId());
            return;
        }

        guild.retrieveMemberById(userId).queue(
                (Member m) -> removeRole(guild, m, role),
                err -> log.error("Cannot remove role {} for member {}: {}", role, userId, err.getMessage())
        );
    }

    private void addRole(Guild guild, Member member, Role role) {
        guild.addRoleToMember(member, role).queue(
                _ -> log.debug("Added role {} to {}", role.getId(), member.getId()),
                err -> log.error("Failed to add role", err)
        );
    }

    private void removeRole(Guild guild, Member member, Role role) {
        guild.removeRoleFromMember(member, role).queue(
                _ -> log.debug("Removed role {} from {}", role.getId(), member.getId()),
                err -> log.error("Failed to remove role", err)
        );
    }
}

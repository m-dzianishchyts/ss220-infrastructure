package club.ss220.manager.shared.application;

import io.github.freya022.botcommands.api.core.BotOwners;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.PrivilegeConfig;
import net.dv8tion.jda.api.interactions.commands.privileges.IntegrationPrivilege;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class CommandService {

    private final BotOwners botOwners;

    public Map<Command.Type, List<Command>> getAvailableCommands(@Nullable Guild guild, User user) {
        JDA jda = user.getJDA();
        if (guild == null) {
            return jda.retrieveCommands().submit()
                    .thenApply(commands -> commands.stream().collect(Collectors.groupingBy(Command::getType)))
                    .join();
        }

        Member member = guild.getMember(user);
        if (member == null) {
            throw new IllegalArgumentException("User is not a member of the guild");
        }

        log.debug("Checking commands for member {} in guild {}", member.getId(), guild.getId());

        CompletableFuture<List<Command>> globalFuture = jda.retrieveCommands().submit();
        CompletableFuture<List<Command>> guildFuture = guild.retrieveCommands().submit();
        CompletableFuture<PrivilegeConfig> privilegesFuture = guild.retrieveCommandPrivileges().submit();
        CompletableFuture.allOf(globalFuture, guildFuture, privilegesFuture).join();
        List<Command> globalCommands = globalFuture.join();
        List<Command> guildCommands = guildFuture.join();
        PrivilegeConfig privilegeConfig = privilegesFuture.join();

        Stream<Command> accessibleCommandsStream = guildCommands.stream()
                .filter(c -> memberHasAccessToCommand(member, c, privilegeConfig.getCommandPrivileges(c)));

        return Stream.concat(globalCommands.stream(), accessibleCommandsStream)
                .collect(Collectors.groupingBy(Command::getType));
    }

    private boolean memberHasAccessToCommand(Member member, Command command, List<IntegrationPrivilege> privileges) {
        if (botOwners.isOwner(member)) {
            return true;
        }

        List<IntegrationPrivilege> safePrivileges = privileges != null ? privileges : List.of();
        boolean isExplicitlyDenied = checkPrivileges(member, safePrivileges, false);
        if (isExplicitlyDenied) {
            return false;
        }
        boolean isExplicitlyAllowed = checkPrivileges(member, safePrivileges, true);
        if (isExplicitlyAllowed) {
            return true;
        }
        return memberHasDefaultPermission(member, command);
    }

    private static boolean checkPrivileges(Member member, List<IntegrationPrivilege> privileges, boolean checkEnabled) {
        for (IntegrationPrivilege privilege : privileges) {
            if (privilege.isEnabled() != checkEnabled) {
                continue;
            }

            final long entityId = privilege.getIdLong();
            boolean matches = switch (privilege.getType()) {
                case USER -> member.getIdLong() == entityId;
                case ROLE -> member.getRoles().stream()
                        .map(Role::getIdLong)
                        .anyMatch(id -> id == entityId);
                // These don't matter
                case CHANNEL, UNKNOWN -> false;
            };

            if (matches) {
                return true;
            }
        }
        return false;
    }

    private static boolean memberHasDefaultPermission(Member member, Command command) {
        if (command.getDefaultPermissions().equals(DefaultMemberPermissions.ENABLED)) {
            return true;
        }
        @SuppressWarnings("DataFlowIssue")
        long permissionsRaw = command.getDefaultPermissions().getPermissionsRaw();
        return member.hasPermission(Permission.getPermissions(permissionsRaw));
    }
}

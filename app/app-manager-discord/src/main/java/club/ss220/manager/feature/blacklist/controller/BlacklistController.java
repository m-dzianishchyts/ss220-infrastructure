package club.ss220.manager.feature.blacklist.controller;

import club.ss220.core.application.AddBlacklistEntryUseCase;
import club.ss220.core.application.GetBlacklistUseCase;
import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.core.shared.NewBlacklistEntry;
import club.ss220.core.shared.UserData;
import club.ss220.core.spi.BlacklistQuery;
import club.ss220.manager.feature.blacklist.view.BlacklistSimpleView;
import club.ss220.manager.feature.blacklist.view.BlacklistVerboseView;
import club.ss220.manager.presentation.Senders;
import club.ss220.manager.shared.GameServerType;
import club.ss220.manager.shared.MemberTarget;
import club.ss220.manager.shared.application.RoleAssignmentService;
import club.ss220.manager.shared.application.UserDataProvider;
import club.ss220.manager.shared.pagination.GenericPaginationController;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlacklistController {

    private static final int PAGE_SIZE = 5;

    private final BlacklistSimpleView view;
    private final BlacklistVerboseView verboseView;
    private final GenericPaginationController paginationController;
    private final UserDataProvider userDataProvider;
    private final GetBlacklistUseCase getBlacklistUseCase;
    private final AddBlacklistEntryUseCase addBlacklistEntryUseCase;
    private final RoleAssignmentService roleAssignmentService;
    private final Senders senders;

    public void show(IReplyCallback interaction,
                     @Nullable MemberTarget playerTarget,
                     @Nullable MemberTarget adminTarget,
                     @Nullable GameServerType serverType,
                     @Nullable Boolean onlyActive) {
        interaction.deferReply().setEphemeral(true).queue();
        InteractionHook hook = interaction.getHook();
        BlacklistQuery.BlacklistQueryBuilder builder = BlacklistQuery.builder();

        if (playerTarget != null) {
            Optional<UserData> player = userDataProvider.getByTarget(playerTarget);
            if (player.isEmpty()) {
                senders.sendEmbedEphemeral(hook, view.renderMemberNotFound(playerTarget));
                return;
            }
            builder.playerDiscordId(player.get().discordId());
        }

        if (adminTarget != null) {
            Optional<UserData> admin = userDataProvider.getByTarget(adminTarget);
            if (admin.isEmpty()) {
                senders.sendEmbedEphemeral(hook, view.renderMemberNotFound(adminTarget));
                return;
            }
            builder.adminDiscordId(admin.get().discordId());
        }

        Optional.ofNullable(serverType).map(GameServerType::name).ifPresent(builder::serverType);
        Optional.ofNullable(onlyActive).ifPresent(builder::activeOnly);

        List<BlacklistEntryData> items = getBlacklistUseCase.execute(builder.build());
        if (items.isEmpty()) {
            senders.sendEmbedEphemeral(hook, view.renderEmpty());
        } else {
            paginationController.show(hook, items, PAGE_SIZE, verboseView);
        }
    }

    public void showMine(IReplyCallback interaction, long discordUserId,
                         @Nullable GameServerType serverType,
                         @Nullable Boolean onlyActive) {
        interaction.deferReply().setEphemeral(true).queue();
        InteractionHook hook = interaction.getHook();

        BlacklistQuery.BlacklistQueryBuilder builder = BlacklistQuery.builder().playerDiscordId(discordUserId);
        Optional.ofNullable(serverType).map(GameServerType::name).ifPresent(builder::serverType);
        Optional.ofNullable(onlyActive).ifPresent(builder::activeOnly);

        List<BlacklistEntryData> items = getBlacklistUseCase.execute(builder.build());
        if (items.isEmpty()) {
            senders.sendEmbedEphemeral(hook, view.renderEmpty());
        } else {
            paginationController.show(hook, items, PAGE_SIZE, view);
        }
    }

    public void addNew(IReplyCallback interaction, MemberTarget playerTarget, User adminUser, GameServerType serverType,
                       int durationDays, String reason, @Nullable Boolean invalidateWhitelist) {
        interaction.deferReply().queue();
        InteractionHook hook = interaction.getHook();

        Optional<UserData> user = userDataProvider.getByTarget(playerTarget);
        if (user.isEmpty()) {
            senders.sendEmbedEphemeral(hook, view.renderMemberNotFound(playerTarget));
            return;
        }
        long playerDiscordId = user.get().discordId();

        NewBlacklistEntry request = NewBlacklistEntry.builder()
                .playerDiscordId(playerDiscordId)
                .adminDiscordId(adminUser.getIdLong())
                .serverType(serverType.name())
                .durationDays(durationDays)
                .reason(reason)
                .invalidateWhitelist(invalidateWhitelist)
                .build();
        BlacklistEntryData bl = addBlacklistEntryUseCase.execute(request);

        Guild guild = hook.getInteraction().getGuild();
        roleAssignmentService.removeWhitelistRole(guild, playerDiscordId, serverType);

        senders.sendEmbed(hook, view.renderCreated(bl));
    }
}

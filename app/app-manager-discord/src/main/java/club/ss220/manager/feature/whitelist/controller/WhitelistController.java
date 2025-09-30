package club.ss220.manager.feature.whitelist.controller;

import club.ss220.core.application.AddWhitelistEntryUseCase;
import club.ss220.core.application.GetWhitelistUseCase;
import club.ss220.core.shared.GameServerType;
import club.ss220.core.shared.NewWhitelistEntry;
import club.ss220.core.shared.UserData;
import club.ss220.core.shared.WhitelistData;
import club.ss220.core.shared.event.WhitelistUpdateEvent;
import club.ss220.core.shared.exception.UserBlacklistedException;
import club.ss220.core.spi.WhitelistQuery;
import club.ss220.manager.feature.whitelist.view.WhitelistSimpleView;
import club.ss220.manager.feature.whitelist.view.WhitelistVerboseView;
import club.ss220.manager.shared.MemberTarget;
import club.ss220.manager.shared.application.UserDataProvider;
import club.ss220.manager.shared.pagination.GenericPaginationController;
import club.ss220.manager.shared.presentation.Senders;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WhitelistController {

    private static final int PAGE_SIZE = 5;

    private final WhitelistSimpleView view;
    private final WhitelistVerboseView verboseView;
    private final GenericPaginationController paginationController;

    private final UserDataProvider userDataProvider;
    private final GetWhitelistUseCase getWhitelistUseCase;
    private final AddWhitelistEntryUseCase addWhitelistEntryUseCase;
    private final ApplicationEventPublisher eventPublisher;

    private final Senders senders;

    public void showWhitelist(IReplyCallback interaction,
                              @Nullable MemberTarget playerTarget,
                              @Nullable MemberTarget adminTarget,
                              @Nullable GameServerType serverType,
                              @Nullable Boolean onlyActive) {
        interaction.deferReply().setEphemeral(true).queue();

        WhitelistQuery.WhitelistQueryBuilder builder = WhitelistQuery.builder();

        if (playerTarget != null) {
            Optional<UserData> player = userDataProvider.getByTarget(playerTarget);
            if (player.isEmpty()) {
                senders.sendEmbed(interaction, view.renderMemberNotFound(playerTarget));
                return;
            }
            builder.playerDiscordId(player.get().discordId());
        }

        if (adminTarget != null) {
            Optional<UserData> admin = userDataProvider.getByTarget(adminTarget);
            if (admin.isEmpty()) {
                senders.sendEmbed(interaction, view.renderMemberNotFound(adminTarget));
                return;
            }
            builder.adminDiscordId(admin.get().discordId());
        }

        Optional.ofNullable(serverType).map(GameServerType::name).ifPresent(builder::serverType);
        Optional.ofNullable(onlyActive).ifPresent(builder::activeOnly);

        List<WhitelistData> items = getWhitelistUseCase.execute(builder.build());

        if (items.isEmpty()) {
            senders.sendEmbed(interaction, view.renderNoEntries());
        } else {
            paginationController.show(interaction, items, PAGE_SIZE, verboseView);
        }
    }

    public void showUserWhitelist(IReplyCallback interaction, long userId,
                                  @Nullable GameServerType serverType,
                                  @Nullable Boolean onlyActive) {
        interaction.deferReply().setEphemeral(true).queue();

        WhitelistQuery.WhitelistQueryBuilder builder = WhitelistQuery.builder().playerDiscordId(userId);
        Optional.ofNullable(serverType).map(GameServerType::name).ifPresent(builder::serverType);
        Optional.ofNullable(onlyActive).ifPresent(builder::activeOnly);

        List<WhitelistData> items = getWhitelistUseCase.execute(builder.build());
        if (items.isEmpty()) {
            senders.sendEmbed(interaction, view.renderNoEntries());
        } else {
            paginationController.show(interaction, items, PAGE_SIZE, view);
        }
    }

    public void addWhitelistEntry(IReplyCallback interaction, MemberTarget playerTarget, User adminUser,
                                  GameServerType serverType, int durationDays, @Nullable Boolean ignoreBlacklist) {
        Optional<UserData> user = userDataProvider.getByTarget(playerTarget);
        if (user.isEmpty()) {
            senders.sendEmbedEphemeral(interaction, view.renderMemberNotFound(playerTarget));
            return;
        }

        long playerDiscordId = user.get().discordId();
        NewWhitelistEntry request = NewWhitelistEntry.builder()
                .playerDiscordId(playerDiscordId)
                .adminDiscordId(adminUser.getIdLong())
                .serverType(serverType.name())
                .durationDays(durationDays)
                .ignoreBlacklist(ignoreBlacklist)
                .build();

        try {
            WhitelistData wl = addWhitelistEntryUseCase.execute(request);
            Guild guild = Objects.requireNonNull(interaction.getGuild());
            eventPublisher.publishEvent(WhitelistUpdateEvent.add(guild.getIdLong(), serverType, playerDiscordId));
            senders.sendEmbed(interaction, view.renderNewEntry(wl));
        } catch (UserBlacklistedException _) {
            senders.sendEmbedEphemeral(interaction, view.renderUserBlacklisted(playerTarget));
        }
    }
}

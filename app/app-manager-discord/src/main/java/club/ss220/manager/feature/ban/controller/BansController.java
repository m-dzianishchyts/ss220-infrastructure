package club.ss220.manager.feature.ban.controller;

import club.ss220.core.application.GetBansUseCase;
import club.ss220.core.shared.BanData;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.MemberData;
import club.ss220.core.spi.BanQuery;
import club.ss220.manager.feature.ban.view.BansView;
import club.ss220.manager.shared.MemberTarget;
import club.ss220.manager.shared.application.MemberDataProvider;
import club.ss220.manager.shared.pagination.GenericPaginationController;
import club.ss220.manager.shared.presentation.Senders;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class BansController {

    private static final int PAGE_SIZE = 5;

    private final BansView view;
    private final GenericPaginationController paginationController;
    private final MemberDataProvider memberDataProvider;
    private final GetBansUseCase getBansUseCase;
    private final Senders senders;

    public void showBans(IReplyCallback interaction,
                         @Nullable MemberTarget playerTarget, @Nullable MemberTarget adminTarget,
                         @Nullable GameServerData server, @Nullable Integer roundId,
                         @Nullable Boolean permanent, @Nullable BanData.BanType banType,
                         @Nullable Boolean expired, @Nullable Boolean unbanned) {
        interaction.deferReply().setEphemeral(true).queue();

        BanQuery.BanQueryBuilder builder = BanQuery.builder();

        if (playerTarget != null) {
            Optional<MemberData> playerData = memberDataProvider.getByTarget(playerTarget);
            if (playerData.isEmpty()) {
                senders.sendEmbed(interaction, view.renderMemberNotFound(playerTarget));
                return;
            }
            builder.ckey(playerData.get().userData().ckey());
        }

        if (adminTarget != null) {
            Optional<MemberData> adminData = memberDataProvider.getByTarget(adminTarget);
            if (adminData.isEmpty()) {
                senders.sendEmbed(interaction, view.renderMemberNotFound(adminTarget));
                return;
            }
            builder.adminCkey(adminData.get().userData().ckey());
        }

        BanQuery query = builder
                .server(server)
                .roundId(roundId)
                .permanent(permanent)
                .banType(banType)
                .expired(expired)
                .unbanned(unbanned)
                .build();

        List<BanData> bans = getBansUseCase.execute(query);
        if (bans.isEmpty()) {
            senders.sendEmbed(interaction, view.renderNoBansFound());
        } else {
            paginationController.show(interaction, bans, PAGE_SIZE, view);
        }
        log.debug("Displayed {} bans for query {}", bans.size(), query);
    }
}
